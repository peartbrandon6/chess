package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ErrorException;
import model.GameData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;

public class MySQLDataAccessTests {
    public MySQLDataAccess makeDataAccess() {
        MySQLDataAccess dataAccess;
        try {
            dataAccess = new MySQLDataAccess();
        } catch (Exception e) {
            throw new RuntimeException("Unable to start MySQL database");
        }
        return dataAccess;
    }



    @Test
    void testGetAuthDataPositive() throws Exception {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO authdata (authtoken, username) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1,"This is an authToken");
            ps.setString(2,"MyUsername");
            ps.executeUpdate();
        }
        var da = makeDataAccess();
        var authdata = da.getAuthData("This is an authToken");

        assertEquals("This is an authToken", authdata.authToken());
        assertEquals("MyUsername", authdata.username());

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testGetAuthDataNegative() {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO authdata (authtoken, username) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1,"authToken");
            ps.setString(2,"Username");
            ps.executeUpdate();
            var da = makeDataAccess();
            var authdata = da.getAuthData("not an authToken");
            assertNull(authdata);

            da.clearAuthData();
            da.clearGameData();
            da.clearUserData();
        } catch (Exception e) {
            fail("Something went wrong with the connection");
        }
    }

    @Test
    void testGetGameDataPositive() throws ErrorException {
        var gson = new Gson();
        var da = makeDataAccess();
        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
        var data = new GameData(1,"john","george","the game", new ChessGame());

        System.out.println(gson.toJson(data.game()));

        da.putGameData(data);

        var gamedata = da.getGameData(1);
        assertInstanceOf(ChessGame.class, gamedata.game());

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testGetGameDataNegative() {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO gamedata (authtoken, username) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1,"authToken");
            ps.setString(2,"Username");
            ps.executeUpdate();
            var da = makeDataAccess();
            da.putGameData(new GameData(1,"john","george","the game", new ChessGame()));
            var gamedata = da.getGameData(1);
            assertNull(gamedata);

            da.clearAuthData();
            da.clearGameData();
            da.clearUserData();
        } catch (Exception e) {
            fail("Something went wrong with the connection");
        }
    }

    @Test
    void testGetAllGameDataPositive() throws ErrorException {
    }

    @Test
    void testGetAllGameDataNegative() {
    }

    @Test
    void testGetUserDataPositive() throws ErrorException {
    }

    @Test
    void testGetUserDataNegative() {
    }

    @Test
    void testPutAuthDataPositive() throws Exception {



    }

    @Test
    void testPutAuthDataNegative() {
    }

    @Test
    void testPutGameDataPositive() throws ErrorException {
    }

    @Test
    void testPutGameDataNegative() {
    }

    @Test
    void testPutUserDataPositive() throws ErrorException {
    }

    @Test
    void testPutUserDataNegative() {
    }

    @Test
    void testClearAuthDataPositive() throws ErrorException {
    }

    @Test
    void testClearGameDataPositive() throws ErrorException {
    }

    @Test
    void testClearUserDataPositive() throws ErrorException {
    }

    @Test
    void testDeleteAuthDataPositive() throws ErrorException {
    }

    @Test
    void testDeleteAuthDataNegative() {
    }
}


