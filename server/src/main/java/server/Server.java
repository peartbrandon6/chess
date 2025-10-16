package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;

import service.UserService;
import model.UserData;


public class Server {

    private final Javalin server;
    private UserService userService;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));


        // Register your endpoints and exception handlers here.
        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", this::register);
    }

    private void register(Context ctx){
        var serializer = new Gson();
        var req = new UserData("bob", "goodpassword", "bob@email.com");
        var res = serializer.toJson(req);
        ctx.result(res);
    }



    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
