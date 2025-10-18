package service;

import dataaccess.DataAccess;

public class Service {
    protected final DataAccess dataAccess;
    public Service(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public boolean authenticate(String authToken){
        return dataAccess.getAuthData(authToken) != null;
    }
}
