package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ErrorException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserService userService;
    private final GameService gameService;
    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }


    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, ctx.session);
                case MAKE_MOVE -> makeMove(gson.fromJson(ctx.message(), MakeMoveCommand.class), ctx.session);
                case LEAVE -> leave(command, ctx.session);
                case RESIGN -> resign(command, ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void sendMessage(ServerMessage message, Session session) throws IOException {
        String msg = gson.toJson(message);
        session.getRemote().sendString(msg);
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        if (invalidUserCheck(command, session)){
            return;
        }
        connections.add(command.getGameID(), session);

        String username;
        try {
            username = userService.getUsername(command.getAuthToken());
        } catch (ErrorException e) {
            sendMessage(new ErrorMessage("Error: unable to authenticate user"), session);
            return;
        }

        ChessGame game;
        try {
            game = gameService.loadGame(command.getGameID());
            if (game == null){
                sendMessage(new ErrorMessage("Error: game does not exist"), session);
                return;
            }
        } catch (Exception e) {
            sendMessage(new ErrorMessage("Error: unable to authenticate game"), session);
            return;
        }

        sendMessage(new LoadGameMessage(game), session);

        var message = String.format("%s joined the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(command.getGameID(), session, notification);
    }




    private void makeMove(MakeMoveCommand command, Session session) throws IOException {
        if (invalidUserCheck(command, session)){
            return;
        }

        ChessGame game;
        try {
            game = gameService.loadGame(command.getGameID());
            if (game == null){
                sendMessage(new ErrorMessage("Error: game does not exist"), session);
                return;
            }
        } catch (Exception e) {
            sendMessage(new ErrorMessage("Error: unable to authenticate game"), session);
            return;
        }

        ChessMove move = command.getMove();
        if (!game.validMoves(move.getStartPosition()).contains(move)){
            sendMessage(new ErrorMessage("Error: invalid move"), session);
            return;
        }

        String username;
        try {
            username = userService.getUsername(command.getAuthToken());
        } catch (ErrorException e) {
            sendMessage(new ErrorMessage("Error: unable to authenticate user"), session);
            return;
        }

        ChessGame newgame;
        try {
            newgame = gameService.makeMove(command.getGameID(), username, move);
        } catch (ErrorException e) {
            sendMessage(new ErrorMessage("Error: unable to make that move"), session);
            return;
        }

        connections.broadcast(command.getGameID(), null, new LoadGameMessage(newgame));

        connections.broadcast(command.getGameID(), session, new NotificationMessage(String.format("%s: %s", username, move)));

        checkEndGame(newgame, command.getGameID());
    }

    private void checkEndGame(ChessGame game, int gameID) throws IOException{
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)){
                connections.broadcast(gameID, null, new NotificationMessage("Checkmate"));
            }
            else if (game.isInCheck(ChessGame.TeamColor.WHITE) || game.isInCheck(ChessGame.TeamColor.BLACK)){
                connections.broadcast(gameID, null, new NotificationMessage("Check"));
            }
            else if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                connections.broadcast(gameID, null, new NotificationMessage("Stalemate"));
            }
    }

    private boolean invalidUserCheck(UserGameCommand command, Session session) throws IOException {
        boolean auth;
        try {
            auth = userService.authenticate(command.getAuthToken());
        } catch (ErrorException e) {
            sendMessage(new ErrorMessage("Error: unable to authenticate user"), session);
            return true;
        }
        if (!auth) {
            sendMessage(new ErrorMessage("Error: not signed in"), session);
            return true;
        }
        return false;
    }

    private void leave(UserGameCommand command, Session session) throws IOException {
        if (connections.connections.get(command.getGameID()).contains(session)){
            String username;
            try {
                username = userService.getUsername(command.getAuthToken());
            } catch (ErrorException e) {
                sendMessage(new ErrorMessage("Error: unable to authenticate user"), session);
                return;
            }
            try {
                gameService.leaveGame(command.getAuthToken(), command.getGameID());
                connections.remove(command.getGameID(), session);
                connections.broadcast(command.getGameID(), session, new NotificationMessage(String.format("%s has left the game", username)));
            } catch (ErrorException e) {
                sendMessage(new ErrorMessage("Error: not in game"), session);
                return;
            }
        }
        else {
            sendMessage(new ErrorMessage("Error: not in game"), session);
        }

    }

    private void resign(UserGameCommand command, Session session) throws IOException {

    }
}

