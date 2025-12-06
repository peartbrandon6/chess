package dataaccess;

import exceptions.ErrorException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryDataAccess implements DataAccess{
    final private HashMap<String, AuthData> authdata = new HashMap<>();
    final private HashMap<Integer, GameData> gamedata = new HashMap<>();
    final private HashMap<String, UserData> userdata = new HashMap<>();

    public AuthData getAuthData(String authToken) {
        return authdata.get(authToken);
    }

    public GameData getGameData(Integer gameID) {
        return gamedata.get(gameID);
    }

    public GameData[] getAllGameData(){
        return gamedata.values().toArray(new GameData[gamedata.size()]);
    }

    public UserData getUserData(String username) {
        return userdata.get(username);
    }

    public HashSet<Integer> getFinishedGames(){ return null; }

    public void putFinishedGame(int gameID) throws ErrorException {    }

    public void putAuthData(AuthData data) {
        authdata.put(data.authToken(), data);
    }

    public void putGameData(GameData data) {
        gamedata.put(data.gameID(), data);
    }

    public void putUserData(UserData data) {
        userdata.put(data.username(),data);
    }

    public void updateGameData(GameData data) {
        gamedata.put(data.gameID(), data);
    }

    public void clearAuthData() {
        authdata.clear();
    }

    public void clearGameData() {
        gamedata.clear();
    }

    public void clearUserData() {
        userdata.clear();
    }

    public void deleteAuthData(String authToken) {
        authdata.remove(authToken);
    }
}
