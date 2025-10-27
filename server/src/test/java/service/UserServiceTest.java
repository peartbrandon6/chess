package service;

import dataaccess.MemoryDataAccess;
import exceptions.ErrorException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void registerPos() throws ErrorException {
        UserData user = new UserData("joe","j@j","j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        AuthData result = service.register(user);

        assertNotNull(result);
        assertEquals(result.username(), user.username());
        assertNotNull(result.authToken());
    }

    @Test
    void registerNeg() throws ErrorException {
        UserData user = new UserData("joe","j@j","j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        AuthData result = service.register(user);

        assertThrows(ErrorException.class, () -> service.register(user));
    }

    @Test
    void loginPos() throws ErrorException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        service.register(new UserData("joe","j@j","j"));
        AuthData result = service.login(user);

        assertNotNull(result);
        assertEquals(result.username(), user.username());
        assertNotNull(result.authToken());
    }

    @Test
    void loginNeg() throws ErrorException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        service.register(new UserData("joe","j@j","j"));
        AuthData result = service.login(user);

        assertThrows(ErrorException.class, () -> service.login(new LoginRequest("johnny","john")));
    }

    @Test
    void logoutPos() throws ErrorException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        service.register(new UserData("joe","j@j","j"));
        AuthData authdata = service.login(user);
        service.logout(authdata.authToken());

        assertDoesNotThrow(() -> service.login(user));
    }

    @Test
    void logoutNeg() throws ErrorException {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        service.register(new UserData("joe","j@j","j"));
        AuthData authdata = service.login(user);
        service.logout(authdata.authToken());

        assertThrows(ErrorException.class, () -> service.logout(authdata.authToken()));
    }
}