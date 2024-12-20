package com.myplayerr.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseManagerTest {

    @Test
    void GivenValidDBUrl_WhenConnectCalled_ThenConnectionIsReturned() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            mockedDriverManager.when(() -> DriverManager.getConnection("jdbc:sqlite:myplayerr.db"))
                    .thenReturn(mockConnection);
            mockedDriverManager.when(DriverManager::getLogWriter).thenReturn(null);

            Connection connection = DatabaseManager.connect();
            assertSame(mockConnection, connection);
            mockedDriverManager.verify(() -> DriverManager.getConnection("jdbc:sqlite:myplayerr.db"), times(1));
        }
    }

    @Test
    void GivenValidConnectionAndStatements_WhenInitializeDatabaseCalled_ThenAllTablesAreCreated() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);
            mockedDriverManager.when(() -> DriverManager.getConnection("jdbc:sqlite:myplayerr.db"))
                    .thenReturn(mockConnection);
            mockedDriverManager.when(DriverManager::getLogWriter).thenReturn(null);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            DatabaseManager.initializeDatabase();
            verify(mockConnection, times(1)).createStatement();
            verify(mockStatement, times(6)).execute(anyString());
            verify(mockStatement, times(1)).execute("CREATE TABLE IF NOT EXISTS artistes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL UNIQUE" +
                    ");");
            verify(mockStatement, times(1)).execute("CREATE TABLE IF NOT EXISTS albums (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "titre TEXT NOT NULL, " +
                    "artiste_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(artiste_id) REFERENCES artistes(id) ON DELETE CASCADE" +
                    ");");
            verify(mockStatement, times(1)).execute("CREATE TABLE IF NOT EXISTS chansons (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "path TEXT NOT NULL UNIQUE, " +
                    "filename TEXT NOT NULL, " +
                    "title TEXT, " +
                    "artist_id INTEGER, " +
                    "album_id INTEGER, " +
                    "duree TEXT, " +
                    "FOREIGN KEY(artist_id) REFERENCES artistes(id) ON DELETE SET NULL, " +
                    "FOREIGN KEY(album_id) REFERENCES albums(id) ON DELETE SET NULL" +
                    ");");
            verify(mockStatement, times(1)).execute("CREATE TABLE IF NOT EXISTS playlists (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL UNIQUE" +
                    ");");
            verify(mockStatement, times(1)).execute("CREATE TABLE IF NOT EXISTS playlist_chansons (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "playlist_id INTEGER NOT NULL, " +
                    "chanson_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(playlist_id) REFERENCES playlists(id), " +
                    "FOREIGN KEY(chanson_id) REFERENCES chansons(id)" +
                    ");");
            verify(mockStatement, times(1)).execute("CREATE TABLE IF NOT EXISTS settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "key TEXT NOT NULL UNIQUE, " +
                    "value TEXT NOT NULL" +
                    ");");
            verify(mockConnection, times(1)).close();
            verify(mockStatement, times(1)).close();
        }
    }

    @Test
    void GivenSQLExceptionDuringStatementExecution_WhenInitializeDatabaseCalled_ThenErrorIsPrinted() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);
            mockedDriverManager.when(() -> DriverManager.getConnection("jdbc:sqlite:myplayerr.db"))
                    .thenReturn(mockConnection);
            mockedDriverManager.when(DriverManager::getLogWriter).thenReturn(null);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            doThrow(new SQLException("Execution failed")).when(mockStatement).execute(anyString());
            DatabaseManager.initializeDatabase();
            verify(mockConnection, times(1)).createStatement();
            verify(mockStatement, times(1)).execute(anyString());
            verify(mockConnection, times(1)).close();
            verify(mockStatement, times(1)).close();
        }
    }
}
