package service;

import dataaccess.MemoryDataAccess;
import datamodel.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register() {
        var user = new UserData("joe","j@j","j");
        var at = "xyz";

        var da = new MemoryDataAccess();
        var service = new UserService(da);
        AuthData res = service.register(user);
        assertNotNull(res);
        assertEquals(res.username(), user.username());
        assertNotNull(res.authToken());
    }
}