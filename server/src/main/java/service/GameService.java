package service;

import dataaccess.DataAccess;
import model.GameData;
import model.JoinGameRequest;
import model.CreateGameRequest;

public class GameService extends Service{
    public GameService(DataAccess dataAccess){
        super(dataAccess);
    }

    public void listGames(String authToken){
        authenticate(authToken);
    }

    public int createGame(String authToken, CreateGameRequest gameName){
        authenticate(authToken);
        return 0;
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest){
        authenticate(authToken);
    }
}
