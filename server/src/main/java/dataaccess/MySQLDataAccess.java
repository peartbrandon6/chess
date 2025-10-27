package dataaccess;

import exceptions.ErrorException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;

public class MySQLDataAccess implements DataAccess{
    private final String[] creationStrings = {
            """
            CREATE TABLE IF NOT EXISTS  authdata (
              `authtoken` varchar(128) NOT NULL,
              `username` varchar(64) NOT NULL,
              PRIMARY KEY (`authtoken`),
              INDEX(name)
            )
            """,

            """
              CREATE TABLE IF NOT EXISTS  userdata (
              `username` varchar(64) NOT NULL,
              `password` varchar(64) NOT NULL,
              'email' varchar(128) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(name)
            )
            """,

            """
            CREATE TABLE IF NOT EXISTS gamedata (
            gameID int NOT NULL,
            whiteusername varchar(64),
            blackusername varchar(64),
            gamename varchar(64) NOT NULL,
            game JSON NOT NULL
            )
            """
    };

    public MySQLDataAccess() throws ErrorException {
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
        //return authdata.get(authToken);
        return null;
    }

    public GameData getGameData(Integer gameID) throws ErrorException{
        return null;
        //return gamedata.get(gameID);
    }

    public GameData[] getAllGameData() throws ErrorException{
        return null;
        //return gamedata.values().toArray(new GameData[gamedata.size()]);
    }

    public UserData getUserData(String username) throws ErrorException{
        return null;
        //return userdata.get(username);
    }

    public void putAuthData(AuthData data) throws ErrorException{
        //authdata.put(data.authToken(), data);
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO authdata (authtoken, username) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1,data.authToken());
            ps.setString(2,data.username());
            ps.executeUpdate();
        } catch(Exception e){
            throw new ErrorException(500, e.getMessage());
        }
    }

    public void putGameData(GameData data) throws ErrorException{
        //gamedata.put(data.gameID(), data);
    }

    public void putUserData(UserData data) throws ErrorException{
        //userdata.put(data.username(),data);
    }

    public void clearAuthData() throws ErrorException{
        //authdata.clear();
    }

    public void clearGameData() throws ErrorException{
        //gamedata.clear();
    }

    public void clearUserData() throws ErrorException{
        //userdata.clear();
    }

    public void deleteAuthData(String authToken) throws ErrorException{
        //authdata.remove(authToken);
    }
}
