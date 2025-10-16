package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

public interface DataAccess {
    public AuthData getAuthData(String authToken);
    public GameData getGameData(Integer gameID);
    public UserData getUserData(String username);

    public void putAuthData(AuthData data);
    public void putGameData(GameData data);
    public void putUserData(UserData data);

    public void clearAuthData();
    public void clearGameData();
    public void clearUserData();

    public void deleteAuthData(String authToken);
}
