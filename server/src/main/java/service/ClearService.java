package service;

import dataaccess.DataAccess;
import exceptions.ServiceException;

public class ClearService extends Service{
    public ClearService(DataAccess dataAccess){
        super(dataAccess);
    }

    public void clear() throws ServiceException {
        try {
            dataAccess.clearAuthData();
            dataAccess.clearGameData();
            dataAccess.clearUserData();
        } catch(Exception e) {
            throw new ServiceException(500, "Error: unauthorized");
        }
    }

}
