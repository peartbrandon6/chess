package client;

import java.net.http.HttpClient;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    ServerFacade(String severUrl){
        this.serverUrl = severUrl;
    }

    private void clear() {
        // clearService.clear();
    }

    private void register() {
        // UserData data = gson.fromJson(ctx.body(), UserData.class);
        // userService.register(data);
    }

    private void login() {
        // LoginRequest data = gson.fromJson(ctx.body(), LoginRequest.class);
        // userService.login(data);
    }

    private void logout() {
        // String auth = gson.fromJson(ctx.header("authorization"), String.class);
        // userService.logout(auth);
    }

    private void listGames() {
        // String auth = gson.fromJson(ctx.header("authorization"), String.class);
        // gameService.listGames(auth);
    }

    private void createGame() {
        // String auth = gson.fromJson(ctx.header("authorization"), String.class);
        // CreateGameRequest req = gson.fromJson(ctx.body(), CreateGameRequest.class);
        // gameService.createGame(auth, req);
    }

    private void joinGame() {
        // String auth = gson.fromJson(ctx.header("authorization"), String.class);
        // JoinGameRequest req = gson.fromJson(ctx.body(), JoinGameRequest.class);
        // gameService.joinGame(auth, req);
    }
}
