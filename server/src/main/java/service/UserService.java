package service;

import dataaccess.DataAccess;
import exceptions.ServiceException;
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

    public AuthData register(UserData userData) throws ServiceException {
        if (dataAccess.getUserData(userData.username()) == null){
            dataAccess.putUserData(userData);
        }
        else throw new ServiceException(403, "Error: already taken");      //CHANGE ME

        String authToken = makeAuthToken();
        AuthData authdata = new AuthData(authToken, userData.username());
        dataAccess.putAuthData(authdata);

        return authdata;
    }

    public AuthData login(LoginRequest userData) throws ServiceException {
        if(userData.username() == null || userData.password() == null){
            throw new ServiceException(400, "Error: unauthorized");
        }

        UserData dbData = dataAccess.getUserData(userData.username());
        if(dbData == null){
            throw new ServiceException(401, "Error: unauthorized");       //CHANGE ME
        }

        if(dbData.password().equals(userData.password())){
            AuthData authData = new AuthData(makeAuthToken(), userData.username());
            dataAccess.putAuthData(authData);
            return authData;
        }
        else{
            throw new ServiceException(401, "Error: unauthorized");      // CHANGE ME
        }
    }

    public void logout(String authToken) throws ServiceException {
        if(authToken == null){
            throw new ServiceException(400, "Error: unauthorized");
        }

        if(authenticate(authToken)) {
            dataAccess.deleteAuthData(authToken);
        }
        else throw new ServiceException(401, "Error: unauthorized");    // CHANGE ME
    }

}
