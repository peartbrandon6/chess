package service;

import dataaccess.MemoryDataAccess;
import exceptions.ServiceException;
import model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Test
    void listGames_pos() throws ServiceException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);
        CreateGameRequest request1 = new CreateGameRequest("game1");
        CreateGameRequest request2 = new CreateGameRequest("game2");
        CreateGameRequest request3 = new CreateGameRequest("game3");

        userService.register(new UserData("joe","j@j","j"));
        AuthData authdata = userService.login(user);
        int gameID1 = gameService.createGame(authdata.authToken(), request1);
        int gameID2 = gameService.createGame(authdata.authToken(), request2);
        int gameID3 = gameService.createGame(authdata.authToken(), request3);

        int[] gameIDs = new int[3];
        gameIDs[0] = gameID1;
        gameIDs[1] = gameID2;
        gameIDs[2] = gameID3;

        for (int x = 0; x <= 2; x++){
            GameData game = gameService.listGames(authdata.authToken())[x];
            assertEquals(gameIDs[x],game.gameID());
        }
    }

    @Test
    void listGames_neg() throws ServiceException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);
        CreateGameRequest request1 = new CreateGameRequest("game1");
        CreateGameRequest request2 = new CreateGameRequest("game2");
        CreateGameRequest request3 = new CreateGameRequest("game3");

        userService.register(new UserData("joe","j@j","j"));
        AuthData authdata = userService.login(user);
        int gameID1 = gameService.createGame(authdata.authToken(), request1);
        int gameID2 = gameService.createGame(authdata.authToken(), request2);
        int gameID3 = gameService.createGame(authdata.authToken(), request3);

        int[] gameIDs = new int[3];
        gameIDs[0] = gameID1;
        gameIDs[1] = gameID2;
        gameIDs[2] = gameID3;

        assertThrows(ServiceException.class, () -> gameService.listGames("I am a fake authToken"));

    }

    @Test
    void createGame_pos() throws ServiceException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);
        CreateGameRequest request = new CreateGameRequest("game1");

        userService.register(new UserData("joe","j@j","j"));
        AuthData authdata = userService.login(user);
        int gameID = gameService.createGame(authdata.authToken(), request);

        assertInstanceOf(Integer.class, gameID);
    }

    @Test
    void createGame_neg() throws ServiceException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);

        CreateGameRequest request = new CreateGameRequest(null); // no gameName

        userService.register(new UserData("joe","j@j","j"));
        AuthData authdata = userService.login(user);

        assertThrows(ServiceException.class, () -> gameService.createGame(authdata.authToken(), request));
    }

    @Test
    void joinGame_pos() throws ServiceException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);
        CreateGameRequest request = new CreateGameRequest("game1");

        userService.register(new UserData("joe","j@j","j"));
        AuthData authdata = userService.login(user);
        int gameID = gameService.createGame(authdata.authToken(), request);
        JoinGameRequest gameRequest = new JoinGameRequest("WHITE", gameID);

        gameService.joinGame(authdata.authToken(),gameRequest);
        GameData game = gameService.listGames(authdata.authToken())[0];

        assertEquals("joe", game.whiteUsername());
    }

    @Test
    void joinGame_neg() throws ServiceException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);
        CreateGameRequest request = new CreateGameRequest("game1");

        userService.register(new UserData("joe","j@j","j"));
        AuthData authdata = userService.login(user);
        int gameID = gameService.createGame(authdata.authToken(), request);
        JoinGameRequest gameRequest = new JoinGameRequest("WHITE", gameID);

        gameService.joinGame(authdata.authToken(),gameRequest);

        assertThrows(ServiceException.class, () -> gameService.joinGame(authdata.authToken(),gameRequest));
    }
}
