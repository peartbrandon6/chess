package client;

import java.net.http.HttpClient;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    ServerFacade(String severUrl){
        this.serverUrl = severUrl;
    }


}
