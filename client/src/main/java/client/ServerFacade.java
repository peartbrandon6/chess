package client;

import com.google.gson.Gson;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson;

    ServerFacade(String severUrl){
        this.serverUrl = severUrl;
        this.gson = new Gson();
    }

    private String sendRequest(HttpRequest request) throws Exception{
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return "Action successful";
        } else {
            var body = httpResponse.body();
            if (body != null) {
                return gson.fromJson(body, Map.class).get("message").toString();
            }
            return "An unknown error occured";
        }
    }

    private String post(String path, String jsonBody, String authToken) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("authorization", authToken)
                .build();

        return sendRequest(request);
    }

    private String delete(String path, String authToken) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .DELETE()
                .header("authorization", authToken)
                .build();

        return sendRequest(request);
    }

    public String clear() throws Exception{
        return delete("/db", "");
    }

    public String register(UserData data) throws Exception{
        return post("/user", gson.toJson(data), "");
    }

    public String login(LoginRequest data) throws Exception{
        return post("/session", gson.toJson(data), "");
    }

    public String logout(String authToken) throws Exception{
        return delete("/db", authToken);
    }

    public void listGames() {
        // String auth = gson.fromJson(ctx.header("authorization"), String.class);
        // gameService.listGames(auth);
    }

    public void createGame() {
        // String auth = gson.fromJson(ctx.header("authorization"), String.class);
        // CreateGameRequest req = gson.fromJson(ctx.body(), CreateGameRequest.class);
        // gameService.createGame(auth, req);
    }

    public void joinGame() {
        // String auth = gson.fromJson(ctx.header("authorization"), String.class);
        // JoinGameRequest req = gson.fromJson(ctx.body(), JoinGameRequest.class);
        // gameService.joinGame(auth, req);
    }
}
