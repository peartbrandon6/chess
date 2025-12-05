package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ErrorException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
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
    private final Gson gson = new Gson();

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
                case MAKE_MOVE -> make_move(command, ctx.session);
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
        boolean auth;
        try {
            auth = userService.authenticate(command.getAuthToken());
        } catch (ErrorException e) {
            sendMessage(new ErrorMessage("Error: unable to authenticate user"), session);
            return;
        }
        if (!auth) {
            sendMessage(new ErrorMessage("Error: not signed in"), session);
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




    private void make_move(UserGameCommand command, Session session) throws IOException {

    }

    private void leave(UserGameCommand command, Session session) throws IOException {

    }

    private void resign(UserGameCommand command, Session session) throws IOException {

    }
}

