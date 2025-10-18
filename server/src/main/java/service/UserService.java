package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;
import model.LoginRequest;

import java.util.UUID;

public class UserService extends Service{
    public UserService(DataAccess dataAccess){
        super(dataAccess);
    }

    private String makeAuthToken(){
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData userData){
        if (dataAccess.getUserData(userData.username()) == null){
            dataAccess.putUserData(userData);
        }
        else throw new RuntimeException("Error: already taken");      //CHANGE ME

        String authToken = makeAuthToken();
        AuthData data = new AuthData(authToken, userData.username());
        dataAccess.putAuthData(data);

        return data;
    }

    public AuthData login(LoginRequest userData){
        UserData dbData = dataAccess.getUserData(userData.username());
        if(dbData == null){
            throw new RuntimeException("401 unauthorized bad username");       //CHANGE ME
        }

        if(dbData.password().equals(userData.password())){
            AuthData authData = new AuthData(makeAuthToken(), userData.username());
            dataAccess.putAuthData(authData);
            return authData;
        }
        else{
            throw new RuntimeException("401 unauthorized bad password");      // CHANGE ME
        }
    }

    public void logout(String authToken){
        if(authenticate(authToken)) {
            dataAccess.deleteAuthData(authToken);
        }
        else throw new RuntimeException("401 forbidden bad authToken");    // CHANGE ME
    }

}
