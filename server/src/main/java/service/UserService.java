package service;

import dataaccess.DataAccess;
import exceptions.ErrorException;
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

    public AuthData register(UserData userData) throws ErrorException {
        if(userData.username() == null || userData.password() == null || userData.email() == null){
            throw new ErrorException(400, "Error: bad request");
        }

        if (dataAccess.getUserData(userData.username()) == null){
            dataAccess.putUserData(userData);
        }
        else { throw new ErrorException(403, "Error: already taken"); }

        String authToken = makeAuthToken();
        AuthData authdata = new AuthData(authToken, userData.username());
        dataAccess.putAuthData(authdata);

        return authdata;
    }

    public AuthData login(LoginRequest userData) throws ErrorException {
        if(userData.username() == null || userData.password() == null){
            throw new ErrorException(400, "Error: bad request");
        }

        UserData dbData = dataAccess.getUserData(userData.username());
        if(dbData == null){
            throw new ErrorException(401, "Error: unauthorized");
        }

        if(dbData.password().equals(userData.password())){
            AuthData authData = new AuthData(makeAuthToken(), userData.username());
            dataAccess.putAuthData(authData);
            return authData;
        }
        else{
            throw new ErrorException(401, "Error: unauthorized");
        }
    }

    public void logout(String authToken) throws ErrorException {
        if(authToken == null){
            throw new ErrorException(400, "Error: bad request");
        }

        if(authenticate(authToken)) {
            dataAccess.deleteAuthData(authToken);
        }
        else { throw new ErrorException(401, "Error: unauthorized"); }
    }

}
