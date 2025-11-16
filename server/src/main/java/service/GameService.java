package service;

import chess.ChessGame;
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
}
