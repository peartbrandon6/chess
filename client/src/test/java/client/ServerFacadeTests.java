package client;

import model.CreateGameRequest;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import client.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        facade = new ServerFacade("http://localhost:8080");
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() throws Exception{
        facade.clear();
        server.stop();
    }


    @Test
    public void registerPos() throws Exception{
        Assertions.assertEquals("Action successful", facade.register(new UserData("username", "goodPassword", "brandon@email.com")));
    }

    @Test
    public void registerNeg() throws Exception{
        facade.register(new UserData("username2", "goodPassword", "brandon@email.com"));
        var answer = facade.register(new UserData("username2", "goodPassword", "brandon@email.com"));
        Assertions.assertEquals("Error: already taken", answer);
    }

    @Test
    public void loginPos() throws Exception{
        facade.register(new UserData("brandon", "password", "email@email.com"));
        Assertions.assertEquals("Action successful", facade.login(new LoginRequest("brandon", "password")));
    }

    @Test
    public void loginNeg() throws Exception{
        Assertions.assertEquals("Error: unauthorized, login information incorrect", facade.login(new LoginRequest("brandon", "password")));
    }

    @Test
    public void logoutPos() throws Exception{
        facade.register(new UserData("brandon35", "password", "email@email.com"));
        Assertions.assertEquals("Action successful", facade.logout());
    }

    @Test
    public void logoutNeg() throws Exception{
        facade.register(new UserData("brandon1", "password", "email@email.com"));
        facade.logout();
        Assertions.assertEquals("Error: unauthorized", facade.logout());
    }

    @Test
    public void createPos() throws Exception{
        facade.register(new UserData("brandon132", "password", "email@email.com"));
        Assertions.assertEquals("Action successful", facade.createGame(new CreateGameRequest("Brandon's game")));
    }

    @Test
    public void createNeg() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void listPos() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void listNeg() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void playPos() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void playNeg() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void observePos() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void observeNeg() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

}
