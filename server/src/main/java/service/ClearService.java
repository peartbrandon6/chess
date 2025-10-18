package service;

import dataaccess.DataAccess;

public class ClearService extends Service{
    public ClearService(DataAccess dataAccess){
        super(dataAccess);
    }

    public void clear(){
        dataAccess.clearAuthData();
        dataAccess.clearGameData();
        dataAccess.clearUserData();
    }

}
