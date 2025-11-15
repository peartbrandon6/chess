package client;

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
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPos() throws Exception{
        Assertions.assertEquals("Action successful", facade.register(new UserData("username", "goodPassword", "brandon@email.com")));
    }

    @Test
    public void loginPos() throws Exception{
        facade.register(new UserData("brandon", "password", "email@email.com"));
        Assertions.assertEquals("Action successful", facade.login(new LoginRequest("brandon", "password")));
    }

    @Test
    public void logoutPos() throws Exception{
        facade.register(new UserData("brandon1", "password", "email@email.com"));
        Assertions.assertEquals("Action successful", facade.logout());
    }

    @Test
    public void createPos() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void listPos() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void playPos() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

    @Test
    public void observePos() throws Exception{
        Assertions.assertEquals("Action successful", facade.clear());
    }

}
