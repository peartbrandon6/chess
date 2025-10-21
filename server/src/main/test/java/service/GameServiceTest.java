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
        var service = new UserService(da);

        service.register(new UserData("joe","j@j","j"));
        AuthData authdata = service.login(user);



        assertDoesNotThrow(() -> service.login(user));
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
}
