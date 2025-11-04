package dataaccess;

import chess.ChessGame;
import exceptions.ErrorException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;

public class MySQLDataAccessTests {
    public MySQLDataAccess makeDataAccess() {
        MySQLDataAccess dataAccess;
        try {
            dataAccess = new MySQLDataAccess();
        } catch (ErrorException e) {
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
        var da = makeDataAccess();
        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
        var data = new GameData(1,"john","george","the game", new ChessGame());

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
            var da = makeDataAccess();
            da.putGameData(new GameData(1,"john","george","the game", new ChessGame()));
            var gamedata = da.getGameData(5);
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
        try (var conn = DatabaseManager.getConnection()){
            var da = makeDataAccess();
            var game1 = new GameData(1,"john","george","the game 1", new ChessGame());
            var game2 = new GameData(2,"billy","bob","the game 2", new ChessGame());
            da.putGameData(game1);
            da.putGameData(game2);

            assertEquals(game1, da.getAllGameData()[0]);
            assertEquals(game2, da.getAllGameData()[1]);

            da.clearAuthData();
            da.clearGameData();
            da.clearUserData();
        } catch (Exception e) {
            fail("Something went wrong with the connection");
        }
    }

    @Test
    void testGetAllGameDataNegative() {
        try (var conn = DatabaseManager.getConnection()){
            var da = makeDataAccess();
            var game3 = new GameData(1,"john","george","the game 1", new ChessGame());
            var game4 = new GameData(2,"billy","bob","the game 2", new ChessGame());
            da.putGameData(game3);
            da.putGameData(game4);

            assertNotEquals(game4, da.getAllGameData()[0]);
            assertNotEquals(game3, da.getAllGameData()[1]);

            da.clearAuthData();
            da.clearGameData();
            da.clearUserData();
        } catch (Exception e) {
            fail("Something went wrong with the connection");
        }
    }

    @Test
    void testGetUserDataPositive() throws ErrorException {
        var da = makeDataAccess();
        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
        var data = new UserData("john","george","email@email.com");

        da.putUserData(data);

        var userdata = da.getUserData("john");
        assertEquals("email@email.com", userdata.email());

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testGetUserDataNegative() {
        try (var conn = DatabaseManager.getConnection()){
            var da = makeDataAccess();
            var user1 = new UserData("john","george","email@email.com");
            var user2 = new UserData("jimmy","password","emailofJimmy@email.com");
            da.putUserData(user1);
            da.putUserData(user2);

            assertNull(da.getUserData("bob?"));

            da.clearAuthData();
            da.clearGameData();
            da.clearUserData();
        } catch (Exception e) {
            fail("Something went wrong with the connection");
        }
    }

    @Test
    void testPutAuthDataPositive() throws Exception {
        var da = makeDataAccess();
        var data = new AuthData("12345", "bob");

        da.putAuthData(data);

        var result = da.getAuthData("12345");

        assertEquals(data, result);

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testPutAuthDataNegative() throws ErrorException{
        var da = makeDataAccess();
        var data = new AuthData("12345", "jimmy");

        da.putAuthData(data);
        assertThrows(ErrorException.class, () -> da.putAuthData(data));

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testPutGameDataPositive() throws ErrorException {
        var da = makeDataAccess();
        var data = new GameData(2,"billy","george","the game 2", new ChessGame());

        da.putGameData(data);

        var result = da.getGameData(2);

        assertEquals(data, result);

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testPutGameDataNegative() throws ErrorException{
        var da = makeDataAccess();
        var data = new GameData(2,"billy","bob","the game 2", new ChessGame());

        da.putGameData(data);
        assertThrows(ErrorException.class, () -> da.putGameData(data));

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testPutUserDataPositive() throws ErrorException {
        var da = makeDataAccess();
        var data = new UserData("john","bobby","email@email.com");

        da.putUserData(data);

        var result = da.getUserData("john");

        assertEquals(data, result);

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testPutUserDataNegative() throws ErrorException{
        var da = makeDataAccess();
        var data = new UserData("john","george","email@email.com");

        da.putUserData(data);
        assertThrows(ErrorException.class, () -> da.putUserData(data));

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testClearAuthDataPositive() throws ErrorException {
        var da = makeDataAccess();
        var data = new AuthData("The authtoken", "the username");

        da.putAuthData(data);

        var authdata = da.getAuthData("The authtoken");

        assertNotNull(authdata);

        da.clearAuthData();

        assertNull(da.getAuthData("The authtoken"));
    }

    @Test
    void testClearGameDataPositive() throws ErrorException {
        var da = makeDataAccess();
        var data = new GameData(1,"john","george","the game", new ChessGame());

        da.putGameData(data);

        var gamedata = da.getGameData(1);

        assertNotNull(gamedata);

        da.clearGameData();

        assertNull(da.getGameData(1));
    }

    @Test
    void testClearUserDataPositive() throws ErrorException {
        var da = makeDataAccess();
        var data = new UserData("the username", "good password", "myemail@email.com");

        da.putUserData(data);

        var userdata = da.getUserData("the username");

        assertNotNull(userdata);

        da.clearUserData();

        assertNull(da.getUserData("the username"));
    }

    @Test
    void testDeleteAuthDataPositive() throws ErrorException {
        var da = makeDataAccess();
        var data = new AuthData("The authtoken", "the username");
        var data2 = new AuthData("authtoken", "username");

        da.putAuthData(data);
        da.putAuthData(data2);

        var authdata = da.getAuthData("The authtoken");

        assertNotNull(authdata);

        da.deleteAuthData("The authtoken");

        assertNull(da.getAuthData("The authtoken"));
        assertNotNull(da.getAuthData("authtoken"));

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }

    @Test
    void testDeleteAuthDataNegative() throws ErrorException{
        var da = makeDataAccess();
        var data = new AuthData("123", "the username");
        var data2 = new AuthData("456", "username");

        da.putAuthData(data);
        da.putAuthData(data2);

        da.deleteAuthData("123");

        assertDoesNotThrow(() -> da.deleteAuthData("123"));

        da.clearAuthData();
        da.clearGameData();
        da.clearUserData();
    }
}


