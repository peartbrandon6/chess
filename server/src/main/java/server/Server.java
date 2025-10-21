package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;

import model.*;
import service.ClearService;
import service.GameService;
import service.UserService;



record ErrorResponse(String message){
}

record CreateGameResponse(int gameID){
}

record ListGamesResponse(GameData[] games){
}

public class Server {

    private final Javalin server;
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
    private final DataAccess dataAccess;
    private final Gson gson;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        dataAccess = new MemoryDataAccess();
        clearService = new ClearService(dataAccess);
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
        gson = new Gson();

        // Register your endpoints and exception handlers here.
        server.delete("db", this::clear);
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
        server.get("game", this::listGames);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
    }

    private void clear(Context ctx){
        try {
            clearService.clear();
            ctx.result("{}");
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private void register(Context ctx){
        UserData data = gson.fromJson(ctx.body(), UserData.class);
        try {
            AuthData res = userService.register(data);
            ctx.result(gson.toJson(res));
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private void login(Context ctx){
        try {
            LoginRequest data = gson.fromJson(ctx.body(), LoginRequest.class);
            AuthData res = userService.login(data);
            ctx.result(gson.toJson(res));
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (JsonSyntaxException e){
            ctx.status(400).result(gson.toJson(new ErrorResponse("Error: bad request")));
        }
    }

    private void logout(Context ctx){
        String auth = gson.fromJson(ctx.header("authorization"), String.class);
        try{
            userService.logout(auth);
            ctx.result("{}");
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        }

    }

    private void listGames(Context ctx){
        String auth = gson.fromJson(ctx.header("authorization"), String.class);
        try{
            GameData[] games = gameService.listGames(auth);
            ctx.result(gson.toJson(new ListGamesResponse(games)));
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private void createGame(Context ctx){
        String auth = gson.fromJson(ctx.header("authorization"), String.class);
        CreateGameRequest req = gson.fromJson(ctx.body(), CreateGameRequest.class);
        try{
            int id = gameService.createGame(auth, req);
            ctx.result(gson.toJson(new CreateGameResponse(id)));
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private void joinGame(Context ctx){
        try{
            String auth = gson.fromJson(ctx.header("authorization"), String.class);
            JoinGameRequest req = gson.fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(auth,req);
            ctx.result("{}");
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (JsonSyntaxException e){
            ctx.status(401).result(gson.toJson(new ErrorResponse("Error: unauthorized")));
        }
    }



    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
