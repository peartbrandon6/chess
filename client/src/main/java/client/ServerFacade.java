package client;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    ServerFacade(String severUrl){
        this.serverUrl = severUrl;
    }

    private String post(String path, String jsonBody, String authToken) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("authorization", authToken)
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return "Action successful";
        } else {
            var body = httpResponse.body();
            if (body != null) {
                return new Gson().fromJson(body, Map.class).get("message").toString();
            }
            return "An unknown error occured";
        }
    }

    private String delete(String path, String authToken) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .DELETE()
                .header("authorization", authToken)
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return "Action successful";
        } else {
            var body = httpResponse.body();
            if (body != null) {
                return new Gson().fromJson(body, Map.class).get("message").toString();
            }
            return "An unknown error occured";
        }
    }

    private String clear() throws Exception{
        return null;
        // clearService.clear();
    }

    private void register() throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl))
                .timeout(java.time.Duration.ofMillis(5000))
                .GET()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
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
