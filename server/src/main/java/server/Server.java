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



public class Server {

    private final Javalin server;
    private final ClearService clearService;
    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;
    private final DataAccess dataAccess;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        dataAccess = new MemoryDataAccess();
        clearService = new ClearService(dataAccess);
        userService = new UserService(dataAccess);
        authService = new AuthService(dataAccess);
        gameService = new GameService(dataAccess);

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
        Gson gson = new Gson();
        try {
            UserData data = gson.fromJson(ctx.body(), UserData.class);
            try {
                AuthData res = userService.register(data);
                ctx.result(gson.toJson(res));
            } catch (ServiceException e){
                String msg = gson.toJson(e.getMessage());
                ctx.status(e.code).result(msg);
            }
        } catch (JsonSyntaxException e){
            ctx.status(400).result("{ \"message\": \"Error: bad request\" }");
        }


    }

    private void login(Context ctx) throws ServiceException {
        Gson gson = new Gson();
        LoginRequest data = gson.fromJson(ctx.body(), LoginRequest.class);
        try {
            AuthData res = userService.login(data);
            ctx.result(gson.toJson(res));
        } catch (ServiceException e){
            ctx.status(e.code).result("{ \"message\": \"Error: unauthorized\" }");
        }
    }

    private void logout(Context ctx) throws ServiceException {
        String data = new Gson().fromJson(ctx.header("authorization"), String.class);
        userService.logout(data);
        ctx.result("{}");
    }





    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
