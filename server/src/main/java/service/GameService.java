package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccess;
import exceptions.ErrorException;
import model.GameData;
import model.JoinGameRequest;
import model.CreateGameRequest;

public class GameService extends Service {
    public GameService(DataAccess dataAccess) {
        super(dataAccess);
    }

    public GameData[] listGames(String authToken) throws ErrorException {
        if (!authenticate(authToken)) {
            throw new ErrorException(401, "Error: unauthorized");
        }
        return dataAccess.getAllGameData();
    }

    private int createGameID() throws ErrorException{
        int id = 1;
        while (dataAccess.getGameData(id) != null) {
            id++;
        }
        return id;
    }

    public int createGame(String authToken, CreateGameRequest createGameRequest) throws ErrorException {
        if (createGameRequest.gameName() == null) {
            throw new ErrorException(400, "Error: bad request");
        }
        if (!authenticate(authToken)) {
            throw new ErrorException(401, "Error: unauthorized");
        }
        int id = createGameID();
        GameData game = new GameData(id, null, null, createGameRequest.gameName(), new ChessGame());
        dataAccess.putGameData(game);

        return id;
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ErrorException {
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == 0) {
            throw new ErrorException(400, "Error: bad request");
        }
        if (!joinGameRequest.playerColor().equals("WHITE") && !joinGameRequest.playerColor().equals("BLACK")) {
            throw new ErrorException(400, "Error: bad request");
        }
        if (!authenticate(authToken)) {
            throw new ErrorException(401, "Error: unauthorized");
        }

        String username = dataAccess.getAuthData(authToken).username();
        GameData game = dataAccess.getGameData(joinGameRequest.gameID());
        if(game == null) { throw new ErrorException(400, "Error: bad request"); }


        // adds player to game
        if(joinGameRequest.playerColor().equals("WHITE") && game.whiteUsername() == null){
            dataAccess.updateGameData(new GameData(game.gameID(), username, game.blackUsername(),game.gameName(),game.game()));
        }
        else if(joinGameRequest.playerColor().equals("BLACK") && game.blackUsername() == null){
            dataAccess.updateGameData(new GameData(game.gameID(), game.whiteUsername(), username,game.gameName(),game.game()));
        }
        else { throw new ErrorException(403, "Error: already taken"); }
    }

    public ChessGame loadGame(int gameID) throws ErrorException{
        return dataAccess.getGameData(gameID).game();
    }

    public void putFinishedGame(int gameID) throws ErrorException{
        dataAccess.putFinishedGame(gameID);
    }

    public String canResign(String authToken, int gameID) throws ErrorException {
        if (authenticate(authToken)) {
            String username = dataAccess.getAuthData(authToken).username();
            GameData gameData = dataAccess.getGameData(gameID);
            if (username.equals(gameData.whiteUsername()) || username.equals(gameData.blackUsername())){
                putFinishedGame(gameID);
                return username;
            }
            else {
                throw new ErrorException(401, "Error: unauthorized");
            }
        }
        else {
            throw new ErrorException(401, "Error: unauthorized");
        }
    }

    public boolean gameOver(int gameID) throws ErrorException{
        return dataAccess.getFinishedGames().contains(gameID);
    }

    public ChessGame makeMove(int gameID, String username, ChessMove move) throws ErrorException {
        GameData gameData = dataAccess.getGameData(gameID);
        var teamTurn = gameData.game().getTeamTurn();
        var piece = gameData.game().getBoard().getPiece(move.getStartPosition());

        if ((gameData.whiteUsername().equals(username) && teamTurn == ChessGame.TeamColor.WHITE
        || gameData.blackUsername().equals(username) && teamTurn == ChessGame.TeamColor.BLACK) && piece != null && teamTurn == piece.getTeamColor()){
            try {
                gameData.game().makeMove(move);
                if(teamTurn == ChessGame.TeamColor.WHITE){
                    gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
                }
                else{
                    gameData.game().setTeamTurn(ChessGame.TeamColor.WHITE);
                }

            } catch (InvalidMoveException e) {
                throw new ErrorException(400, "Error: bad request");
            }
            dataAccess.updateGameData(gameData);
            return gameData.game();
        }
        else {
            throw new ErrorException(400, "Error: bad request");
        }
    }

    public void leaveGame(String authToken, int gameID) throws ErrorException {
        if (authenticate(authToken)) {
            String username = dataAccess.getAuthData(authToken).username();
            GameData gameData = dataAccess.getGameData(gameID);
            if (username.equals(gameData.whiteUsername())){
                gameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            }
            else if (username.equals(gameData.blackUsername())){
                gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            }

            dataAccess.updateGameData(gameData);
        }
        else {
            throw new ErrorException(401, "Error: unauthorized");
        }
    }
}
