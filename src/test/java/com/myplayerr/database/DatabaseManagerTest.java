package com.myplayerr.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseManagerTest {

    @Test
    public void testDatabaseConnection() throws SQLException {
        Connection connection = DatabaseManager.connect();
        assertNotNull(connection, "Connection should not be null");
    }

    @Test
    public void testDatabaseInitialization() {
        DatabaseManager.initializeDatabase();
    }
}
