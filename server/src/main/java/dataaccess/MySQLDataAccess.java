package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ErrorException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;

public class MySQLDataAccess implements DataAccess{
    private final Gson gson;

    private final String[] creationStrings = {
            """
            CREATE TABLE IF NOT EXISTS  authdata (
              authtoken varchar(128) NOT NULL,
              username varchar(64) NOT NULL,
              PRIMARY KEY (authtoken),
              INDEX(username)
            )
            """,

            """
            CREATE TABLE IF NOT EXISTS  userdata (
              username varchar(64) NOT NULL,
              password varchar(64) NOT NULL,
              email varchar(128) NOT NULL,
              PRIMARY KEY (username),
              INDEX(username)
            )
            """,

            """
            CREATE TABLE IF NOT EXISTS gamedata (
                gameID int NOT NULL AUTO_INCREMENT,
                whiteusername varchar(64),
                blackusername varchar(64),
                gamename varchar(64) NOT NULL,
                game JSON NOT NULL,
                PRIMARY KEY (gameID)
            )
            """
    };

    public MySQLDataAccess() throws ErrorException {
        gson = new Gson();
        DatabaseManager.createDatabase();
        try(var connection = DatabaseManager.getConnection()){
            for (String statement : creationStrings){
                try( var preparedStatement = connection.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
    }

    public AuthData getAuthData(String authToken) throws ErrorException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM authdata WHERE authtoken = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet set = ps.executeQuery()) {
                    if (set.next()) {
                        String auth = set.getString(1);
                        String username = set.getString(2);
                        return new AuthData(auth, username);
                    }
                }
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
        return null;
    }

    public GameData getGameData(Integer gameID) throws ErrorException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM gamedata WHERE gameid = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet set = ps.executeQuery()) {
                    if (set.next()) {
                        String whiteUsername = set.getString(2);
                        String blackUsername = set.getString(3);
                        String gameName = set.getString(4);
                        ChessGame game = gson.fromJson(set.getString(5), ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
        return null;
    }

    public GameData[] getAllGameData() throws ErrorException{
        GameData[] allGameData = new GameData[0];
        try (var conn = DatabaseManager.getConnection()){
            try (var stmt = conn.prepareStatement("SELECT COUNT(*) FROM gamedata")) {
                try (ResultSet rs = stmt.executeQuery()){
                    if (rs.next()) {
                        int length = rs.getInt(1);
                        allGameData = new GameData[length];
                    }
                }
            }
            var statement = "SELECT * FROM gamedata";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet set = ps.executeQuery()) {
                    int i = 0;
                    while (set.next()) {
                        int gameID = set.getInt(1);
                        String whiteUsername = set.getString(2);
                        String blackUsername = set.getString(3);
                        String gameName = set.getString(4);
                        ChessGame game = gson.fromJson(set.getString(5), ChessGame.class);
                        allGameData[i] = new GameData(gameID,whiteUsername,blackUsername,gameName,game);
                        i++;
                    }
                }
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
        return allGameData;
    }

    public UserData getUserData(String username) throws ErrorException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM userdata WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet set = ps.executeQuery()) {
                    if (set.next()) {
                        String password = set.getString(2);
                        String email = set.getString(3);
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
        return null;
    }

    public void putAuthData(AuthData data) throws ErrorException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO authdata (authtoken, username) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, data.authToken());
                ps.setString(2, data.username());
                ps.executeUpdate();
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
    }

    public void putGameData(GameData data) throws ErrorException{
        var statement = "INSERT INTO gamedata (gameid, whiteusername, blackusername, gamename, game) VALUES (?, ?, ?, ?, ?)";
        String json = gson.toJson(data);
        try (var conn = DatabaseManager.getConnection()){


            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, data.gameID());
                ps.setString(2, data.whiteUsername());
                ps.setString(3, data.blackUsername());
                ps.setString(4, data.gameName());
                ps.setString(5, json);
                ps.executeUpdate();
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
    }

    public void updateGameData(GameData data) throws ErrorException{
        var statement = "UPDATE gamedata SET whiteusername = ?, blackusername = ?, game = ? WHERE gameid = ?";
        String json = gson.toJson(data);
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, data.whiteUsername());
                ps.setString(2, data.blackUsername());
                ps.setString(3, json);
                ps.setInt(4, data.gameID());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new ErrorException(500, e.getMessage());
        }
    }

    public void putUserData(UserData data) throws ErrorException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, data.username());
                ps.setString(2, data.password());
                ps.setString(3, data.email());
                ps.executeUpdate();
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
    }

    private void execute(String sql) throws ErrorException{
        try (var conn = DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate(sql);
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
    }

    public void clearAuthData() throws ErrorException{
        execute("DELETE FROM authdata");
    }

    public void clearGameData() throws ErrorException{
        execute("DELETE FROM gamedata");
    }

    public void clearUserData() throws ErrorException{
        execute("DELETE FROM userdata");
    }

    public void deleteAuthData(String authToken) throws ErrorException{
        try (var conn = DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement("DELETE FROM authdata WHERE authtoken = ?")) {
                statement.setString(1, authToken);
                statement.executeUpdate();
            }
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
    }
}
