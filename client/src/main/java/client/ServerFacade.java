package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import ui.DrawBoard;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

record ListGamesResponse(GameData[] games){
}

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson;
    private String authToken = "";
    private GameData[] currentGames = new GameData[0];

    ServerFacade(String severUrl){
        this.serverUrl = severUrl;
        this.gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .create();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception{
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String handleResponse(HttpResponse<String> response) throws Exception{
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return "Action successful";
        } else {
            var body = response.body();
            if (body != null) {
                return gson.fromJson(body, Map.class).get("message").toString();
            }
            return "An unknown error occurred";
        }
    }


    private HttpResponse<String> post(String path, String jsonBody) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("authorization", authToken)
                .build();

        return sendRequest(request);
    }

    private HttpResponse<String> get(String path) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .GET()
                .header("authorization", authToken)
                .build();

        return sendRequest(request);
    }

    private HttpResponse<String> put(String path, String jsonBody) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("authorization", authToken)
                .build();

        return sendRequest(request);
    }

    private HttpResponse<String> delete(String path) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverUrl + path))
                .DELETE()
                .header("authorization", authToken)
                .build();

        return sendRequest(request);
    }

    public String clear() throws Exception{
        var response = delete("/db");
        return handleResponse(response);
    }

    public String register(UserData data) throws Exception{
        var response = post("/user", gson.toJson(data));
        String json = response.body();
        var map = gson.fromJson(json, Map.class);
        if (map.containsKey("authToken")) {
            this.authToken = map.get("authToken").toString();
        }
        return handleResponse(response);
    }

    public String login(LoginRequest data) throws Exception{
        var response = post("/session", gson.toJson(data));
        String json = response.body();
        var map = gson.fromJson(json, Map.class);
        if (map.containsKey("authToken")) {
            this.authToken = map.get("authToken").toString();
        }
        return handleResponse(response);
    }

    public String logout() throws Exception{
        var response = delete("/session");
        return handleResponse(response);
    }

    public String createGame(CreateGameRequest data) throws Exception{
        var response = post("/game", gson.toJson(data));
        return handleResponse(response);
    }

    public String listGames() throws Exception{
        var response = get("/game");
        String json = response.body();
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            var games = gson.fromJson(json, ListGamesResponse.class);
            this.currentGames = games.games();
            StringBuilder listText = new StringBuilder("All Games:\nUse the id number to join a game");

            for (int i = 0; i < games.games().length; i++){
                String white = "";
                String black = "";
                if(games.games()[i].whiteUsername() != null){
                    white = games.games()[i].whiteUsername();
                }
                if(games.games()[i].blackUsername() != null){
                    black = games.games()[i].blackUsername();
                }
                listText.append(String.format("""
                                \nid - %d
                                Game name - %s
                                White player - %s
                                Black player - %s
                                """,
                        i + 1, games.games()[i].gameName(), white, black));
            }
            return listText.toString();
        } else {
            var body = response.body();
            if (body != null) {
                return gson.fromJson(body, Map.class).get("message").toString();
            }
            return "An unknown error occurred";
        }
    }

    public String joinGame(JoinGameRequest data) throws Exception{
        JoinGameRequest adjustedData;
        try{
            adjustedData = new JoinGameRequest(data.playerColor(), currentGames[data.gameID()-1].gameID());
        } catch (Exception e) {
            throw new Exception("Invalid game ID");
        }

        var response = put("/game", gson.toJson(adjustedData));
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            ChessGame game = new ChessGame();
            game.setBoard(new ChessBoard());
            ChessBoard board = game.getBoard();
            board.resetBoard();

            ChessGame.TeamColor color;
            if(data.playerColor().equals("WHITE")){
                color = ChessGame.TeamColor.WHITE;
            }
            else{
                color = ChessGame.TeamColor.BLACK;
            }

            DrawBoard.drawBoard(color, board);
            return "";
        } else {
            var body = response.body();
            if (body != null) {
                return gson.fromJson(body, Map.class).get("message").toString();
            }
            return "An unknown error occurred";
        }
    }

}
