package service;

import dataaccess.DataAccess;
import model.GameData;
import model.JoinRequest;

public class GameService extends Service{
    public GameService(DataAccess dataAccess){
        super(dataAccess);
    }

    public void listGames(String authToken){
        authenticate(authToken);
    }

    public GameData createGame(String authToken, String gameName){
        authenticate(authToken);
        return null;
    }

    public void joinGame(String authToken, JoinRequest joinRequest){
        authenticate(authToken);
    }
}
