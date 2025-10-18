package service;

import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register() {
        UserData user = new UserData("joe","j@j","j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        AuthData result = service.register(user);

        assertNotNull(result);
        assertEquals(result.username(), user.username());
        assertNotNull(result.authToken());
    }

    @Test
    void login() {
        LoginRequest user = new LoginRequest("joe","j@j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        service.register(new UserData("joe","j@j","j"));
        AuthData result = service.login(user);

        assertNotNull(result);
        assertEquals(result.username(), user.username());
        assertNotNull(result.authToken());
    }


}