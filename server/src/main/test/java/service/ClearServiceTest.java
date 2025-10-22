package service;

import dataaccess.MemoryDataAccess;
import exceptions.ServiceException;
import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    @Test
    void clearPos() throws ServiceException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var userService = new UserService(da);
        var gameService = new GameService(da);
        var clearService = new ClearService(da);
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

        clearService.clear();

        assertThrows(ServiceException.class, () -> gameService.listGames(authdata.authToken()));
    }

    /*
    @Test
    void clearNeg() throws ServiceException {
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

        dataaccess.MemoryDataAccess badDataAccess = null;
        var clearService = new ClearService(badDataAccess);

        assertThrows(ServiceException.class, () -> clearService.clear());
    }
    */
}
