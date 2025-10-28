package dataaccess;

import dataaccess.MySQLDataAccess;
import exceptions.ErrorException;
import model.*;
import org.junit.jupiter.api.Test;

public class MySQLDataAccessTests {
    public MySQLDataAccess makeDataAccess() {
        MySQLDataAccess dataAccess;
        try {
            dataAccess = new MySQLDataAccess();
        } catch (Exception e) {
            throw new RuntimeException("Unable to start MySQL database");
        }
        return dataAccess;
    }

    @Test
    void testGetAuthDataPositive() throws ErrorException {
    }

    @Test
    void testGetAuthDataNegative() {
    }

    @Test
    void testGetGameDataPositive() throws ErrorException {
    }

    @Test
    void testGetGameDataNegative() {
    }

    @Test
    void testGetAllGameDataPositive() throws ErrorException {
    }

    @Test
    void testGetAllGameDataNegative() {
    }

    @Test
    void testGetUserDataPositive() throws ErrorException {
    }

    @Test
    void testGetUserDataNegative() {
    }

    @Test
    void testPutAuthDataPositive() throws ErrorException {
    }

    @Test
    void testPutAuthDataNegative() {
    }

    @Test
    void testPutGameDataPositive() throws ErrorException {
    }

    @Test
    void testPutGameDataNegative() {
    }

    @Test
    void testPutUserDataPositive() throws ErrorException {
    }

    @Test
    void testPutUserDataNegative() {
    }

    @Test
    void testClearAuthDataPositive() throws ErrorException {
    }

    @Test
    void testClearGameDataPositive() throws ErrorException {
    }

    @Test
    void testClearUserDataPositive() throws ErrorException {
    }

    @Test
    void testDeleteAuthDataPositive() throws ErrorException {
    }

    @Test
    void testDeleteAuthDataNegative() {
    }
}


