package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;

import model.*;
import service.AuthService;
import service.ClearService;
import service.GameService;
import service.UserService;



record ErrorResponse(String message){
}

public class Server {

    private final Javalin server;
    private final ClearService clearService;
    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;
    private final DataAccess dataAccess;
    private final Gson gson;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        dataAccess = new MemoryDataAccess();
        clearService = new ClearService(dataAccess);
        userService = new UserService(dataAccess);
        authService = new AuthService(dataAccess);
        gameService = new GameService(dataAccess);
        gson = new Gson();

        // Register your endpoints and exception handlers here.
        server.delete("db", this::clear);
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
    }

    private void clear(Context ctx){
        clearService.clear();
        ctx.result("{}");
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

    private void login(Context ctx) throws ServiceException {
        LoginRequest data = gson.fromJson(ctx.body(), LoginRequest.class);
        try {
            AuthData res = userService.login(data);
            ctx.result(gson.toJson(res));
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
        }
    }

    private void logout(Context ctx) throws ServiceException {
        String data = gson.fromJson(ctx.header("authorization"), String.class);
        try{
            userService.logout(data);
            ctx.result("{}");
        } catch (ServiceException e){
            ctx.status(e.code).result(gson.toJson(new ErrorResponse(e.getMessage())));
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
