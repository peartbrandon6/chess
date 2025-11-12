package client;
import client.ServerFacade;

public class Repl {
    private final ServerFacade server;
    private final State state;

    Repl(String url){
        server = new ServerFacade(url);
        state = State.SIGNEDOUT;
    }
}
