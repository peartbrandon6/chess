package service;

import dataaccess.DataAccess;
import exceptions.ErrorException;

public class ClearService extends Service{
    public ClearService(DataAccess dataAccess){
        super(dataAccess);
    }

    public void clear() throws ErrorException {
        try {
            dataAccess.clearAuthData();
            dataAccess.clearGameData();
            dataAccess.clearUserData();
        } catch(Exception e) {
            throw new ErrorException(500, e.getMessage());
        }
    }

}
