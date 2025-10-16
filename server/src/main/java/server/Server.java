package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;

import model.AuthData;
import service.AuthService;
import service.ClearService;
import service.GameService;
import service.UserService;
import model.UserData;


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
    }

    private void clear(Context ctx){
        clearService.clear();
        ctx.result("{}");
    }

    private void register(Context ctx){
        UserData data = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData res = userService.register(data);
        ctx.result(new Gson().toJson(res));
    }





    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
