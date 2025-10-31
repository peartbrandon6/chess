package dataaccess;

import exceptions.ErrorException;
import model.AuthData;
import model.GameData;
import model.UserData;

public interface DataAccess {
    public AuthData getAuthData(String authToken) throws ErrorException;
    public GameData getGameData(Integer gameID) throws ErrorException;
    public GameData[] getAllGameData() throws ErrorException;
    public UserData getUserData(String username) throws ErrorException;

    public void putAuthData(AuthData data) throws ErrorException;
    public void putGameData(GameData data) throws ErrorException;
    public void putUserData(UserData data) throws ErrorException;
    public void updateGameData(GameData data) throws ErrorException;

    public void clearAuthData() throws ErrorException;
    public void clearGameData() throws ErrorException;
    public void clearUserData() throws ErrorException;

    public void deleteAuthData(String authToken) throws ErrorException;
}
