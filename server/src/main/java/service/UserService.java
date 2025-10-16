package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    private String makeAuthToken(){
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData userData){
        if (dataAccess.getUserData(userData.username()) == null){
            dataAccess.putUserData(userData);
        }
        else throw new RuntimeException("Error: already taken");

        String authToken = makeAuthToken();
        AuthData data = new AuthData(authToken, userData.username());
        dataAccess.putAuthData(data);

        return data;
    }

    public AuthData login(){
        return null;
    }

    public void logout(){

    }

}
