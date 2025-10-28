package service;

import dataaccess.DataAccess;
import exceptions.ErrorException;

public class Service {
    protected final DataAccess dataAccess;
    public Service(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public boolean authenticate(String authToken) throws ErrorException {
        return dataAccess.getAuthData(authToken) != null;
    }
}
