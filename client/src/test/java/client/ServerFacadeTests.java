package client;

import model.CreateGameRequest;
import model.JoinGameRequest;
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


    @AfterEach
    void clearFacade() throws Exception{
        facade.clear();
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
        var res = facade.createGame(new CreateGameRequest("unregisteredGame")).contains("Error");
        Assertions.assertTrue(res);
    }

    @Test
    public void listPos() throws Exception{
        facade.clear();
        facade.register(new UserData("brandon66", "password", "email@email.com"));
        facade.createGame(new CreateGameRequest("theGame"));
        Assertions.assertDoesNotThrow(() -> facade.listGames());
    }

    @Test
    public void listNeg() throws Exception{
        var test_facade = new ServerFacade("http://localhost:8080");
        Assertions.assertTrue(test_facade.listGames().contains("Error"));
    }

    @Test
    public void joinPos() throws Exception{
        facade.register(new UserData("brandon011", "password", "email@email.com"));
        facade.createGame(new CreateGameRequest("join me"));
        facade.listGames();
        Assertions.assertEquals("", facade.joinGame(new JoinGameRequest("WHITE", 1)));
    }

    @Test
    public void joinNeg() throws Exception{
        facade.register(new UserData("brandon014", "password", "email@email.com"));
        facade.createGame(new CreateGameRequest("yay"));
        facade.listGames();
        Assertions.assertEquals("Error: Invalid game ID", facade.joinGame(new JoinGameRequest("WHITE", 4)));
    }

    @Test
    public void observePos() throws Exception{
        var test_facade = new ServerFacade("http://localhost:8080");
        test_facade.register(new UserData("brandon011246", "password", "email@email.com"));
        test_facade.createGame(new CreateGameRequest("okidoki"));
        test_facade.listGames();
        Assertions.assertEquals("Currently observing game #1", test_facade.observeGame(1));
    }

    @Test
    public void observeNeg() throws Exception{
        var test_facade = new ServerFacade("http://localhost:8080");
        test_facade.register(new UserData("brandon01134", "password", "email@email.com"));
        test_facade.createGame(new CreateGameRequest("cheese"));
        test_facade.listGames();
        Assertions.assertEquals("Invalid game ID", test_facade.observeGame(6));
    }

}
