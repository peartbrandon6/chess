package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
    final private HashMap<String, AuthData> authdata = new HashMap<>();
    final private HashMap<Integer, GameData> gamedata = new HashMap<>();
    final private HashMap<String, UserData> userdata = new HashMap<>();

    @Override
    public AuthData getAuthData(String authToken) {
        return authdata.get(authToken);
    }

    @Override
    public GameData getGameData(Integer gameID) {
        return gamedata.get(gameID);
    }

    @Override
    public UserData getUserData(String username) {
        return userdata.get(username);
    }

    @Override
    public void putAuthData(AuthData data) {
        authdata.put(data.authToken(), data);
    }

    @Override
    public void putGameData(GameData data) {
        gamedata.put(data.gameID(), data);
    }

    @Override
    public void putUserData(UserData data) {
        userdata.put(data.username(),data);
    }

    @Override
    public void clearAuthData() {
        userdata.clear();
    }

    @Override
    public void clearGameData() {
        gamedata.clear();
    }

    @Override
    public void clearUserData() {
        userdata.clear();
    }

    @Override
    public void deleteAuthData(String authToken) {
        authdata.remove(authToken);
    }
}
