package com.myplayerr.database;

import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumDAOTest {

    @Mock
    ArtisteDAO mockArtisteDAO;

    @Test
    void GivenExistingAlbumNameAndArtistId_WhenGetAlbumByNameAndArtistCalled_ThenReturnAlbum() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("SELECT * FROM albums WHERE titre = ? AND artiste_id = ?")).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getString("titre")).thenReturn("TestAlbum");

            AlbumDAO albumDAO = new AlbumDAO();
            Album album = albumDAO.getAlbumByNameAndArtist("TestAlbum", 1);

            assertNotNull(album);
            assertEquals(1, album.getId());
            assertEquals("TestAlbum", album.getNom());

            verify(mockPreparedStatement, times(1)).setString(1, "TestAlbum");
            verify(mockPreparedStatement, times(1)).setInt(2, 1);
            verify(mockResultSet, times(1)).next();
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockResultSet, times(1)).close();
        }
    }

    @Test
    void GivenNonExistingAlbumNameAndArtistId_WhenGetAlbumByNameAndArtistCalled_ThenReturnNull() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("SELECT * FROM albums WHERE titre = ? AND artiste_id = ?")).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            AlbumDAO albumDAO = new AlbumDAO();
            Album album = albumDAO.getAlbumByNameAndArtist("NonExistingAlbum", 1);

            assertNull(album);

            verify(mockPreparedStatement, times(1)).setString(1, "NonExistingAlbum");
            verify(mockPreparedStatement, times(1)).setInt(2, 1);
            verify(mockResultSet, times(1)).next();
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockResultSet, times(1)).close();
        }
    }

    @Test
    void GivenSQLException_WhenGetAlbumByNameAndArtistCalled_ThenReturnNull() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            mockedDatabaseManager.when(DatabaseManager::connect).thenThrow(new SQLException("Connection failed"));

            AlbumDAO albumDAO = new AlbumDAO();
            Album album = albumDAO.getAlbumByNameAndArtist("TestAlbum", 1);

            assertNull(album);
            mockedDatabaseManager.verify(DatabaseManager::connect, times(1));
        }
    }

    @Test
    void GivenValidAlbumNameAndArtistId_WhenAddAlbumCalled_ThenExecuteUpdate() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("INSERT OR IGNORE INTO albums (titre, artiste_id) VALUES (?, ?)")).thenReturn(mockPreparedStatement);

            AlbumDAO albumDAO = new AlbumDAO();
            albumDAO.addAlbum("NewAlbum", 2);

            verify(mockPreparedStatement, times(1)).setString(1, "NewAlbum");
            verify(mockPreparedStatement, times(1)).setInt(2, 2);
            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
        }
    }

    @Test
    void GivenSQLException_WhenAddAlbumCalled_ThenHandleException() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("INSERT OR IGNORE INTO albums (titre, artiste_id) VALUES (?, ?)")).thenReturn(mockPreparedStatement);
            doThrow(new SQLException("Insert failed")).when(mockPreparedStatement).executeUpdate();

            AlbumDAO albumDAO = new AlbumDAO();
            albumDAO.addAlbum("FailAlbum", 3);

            verify(mockPreparedStatement, times(1)).setString(1, "FailAlbum");
            verify(mockPreparedStatement, times(1)).setInt(2, 3);
            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
        }
    }

    @Test
    void GivenAlbumsExist_WhenGetAllAlbumsCalled_ThenReturnListOfAlbums() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery("SELECT albums.id, albums.titre, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums JOIN artistes ON albums.artiste_id = artistes.id")).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, true, false);
            when(mockResultSet.getInt("id")).thenReturn(1, 2);
            when(mockResultSet.getString("titre")).thenReturn("Album1", "Album2");
            when(mockResultSet.getString("artiste_nom")).thenReturn("Artiste1", "Artiste2");
            when(mockResultSet.getInt("artiste_id")).thenReturn(1, 2);

            AlbumDAO albumDAO = new AlbumDAO();
            List<Album> albums = albumDAO.getAllAlbums();

            assertNotNull(albums);
            assertEquals(2, albums.size());
            assertEquals("Album1", albums.get(0).getNom());
            assertEquals("Artiste1", albums.get(0).getArtiste().getNom());
            assertEquals("Album2", albums.get(1).getNom());
            assertEquals("Artiste2", albums.get(1).getArtiste().getNom());

            verify(mockStatement, times(1)).executeQuery("SELECT albums.id, albums.titre, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums JOIN artistes ON albums.artiste_id = artistes.id");
            verify(mockConnection, times(1)).createStatement();
            verify(mockResultSet, times(3)).next();
            verify(mockConnection, times(1)).close();
            verify(mockStatement, times(1)).close();
            verify(mockResultSet, times(1)).close();
        }
    }

    @Test
    void GivenNoAlbumsExist_WhenGetAllAlbumsCalled_ThenReturnEmptyList() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery("SELECT albums.id, albums.titre, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums JOIN artistes ON albums.artiste_id = artistes.id")).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            AlbumDAO albumDAO = new AlbumDAO();
            List<Album> albums = albumDAO.getAllAlbums();

            assertNotNull(albums);
            assertTrue(albums.isEmpty());

            verify(mockStatement, times(1)).executeQuery("SELECT albums.id, albums.titre, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums JOIN artistes ON albums.artiste_id = artistes.id");
            verify(mockConnection, times(1)).createStatement();
            verify(mockResultSet, times(1)).next();
            verify(mockConnection, times(1)).close();
            verify(mockStatement, times(1)).close();
            verify(mockResultSet, times(1)).close();
        }
    }

    @Test
    void GivenSQLException_WhenGetAllAlbumsCalled_ThenReturnEmptyList() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            Statement mockStatement = mock(Statement.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery("SELECT albums.id, albums.titre, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums JOIN artistes ON albums.artiste_id = artistes.id")).thenThrow(new SQLException("Query failed"));

            AlbumDAO albumDAO = new AlbumDAO();
            List<Album> albums = albumDAO.getAllAlbums();

            assertNotNull(albums);
            assertTrue(albums.isEmpty());

            verify(mockStatement, times(1)).executeQuery("SELECT albums.id, albums.titre, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums JOIN artistes ON albums.artiste_id = artistes.id");
            verify(mockConnection, times(1)).createStatement();
            verify(mockConnection, times(1)).close();
            verify(mockStatement, times(1)).close();
        }
    }

    @Test
    void GivenExistingAlbumId_WhenGetAlbumByIdCalled_ThenReturnAlbum() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);
            Artiste mockArtiste = mock(Artiste.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("SELECT * FROM albums WHERE id = ?")).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getString("titre")).thenReturn("AlbumTitle");
            when(mockResultSet.getInt("artiste_id")).thenReturn(1);
            when(mockArtisteDAO.getArtisteById(1)).thenReturn(mockArtiste);

            AlbumDAO albumDAO = new AlbumDAO();
            albumDAO.setDependance(mockArtisteDAO);
            Album album = albumDAO.getAlbumById(1);

            assertNotNull(album);
            assertEquals(1, album.getId());
            assertEquals("AlbumTitle", album.getNom());
            assertEquals(mockArtiste, album.getArtiste());

            verify(mockPreparedStatement, times(1)).setInt(1, 1);
            verify(mockResultSet, times(1)).next();
            verify(mockArtisteDAO, times(1)).getArtisteById(1);
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockResultSet, times(1)).close();
        }
    }

    @Test
    void GivenNonExistingAlbumId_WhenGetAlbumByIdCalled_ThenReturnNull() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("SELECT * FROM albums WHERE id = ?")).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            AlbumDAO albumDAO = new AlbumDAO();
            albumDAO.setDependance(mockArtisteDAO);
            Album album = albumDAO.getAlbumById(999);

            assertNull(album);

            verify(mockPreparedStatement, times(1)).setInt(1, 999);
            verify(mockResultSet, times(1)).next();
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockResultSet, times(1)).close();
            verifyNoInteractions(mockArtisteDAO);
        }
    }

    @Test
    void GivenSQLException_WhenGetAlbumByIdCalled_ThenReturnNull() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            mockedDatabaseManager.when(DatabaseManager::connect).thenThrow(new SQLException("Connection failed"));

            AlbumDAO albumDAO = new AlbumDAO();
            albumDAO.setDependance(mockArtisteDAO);
            Album album = albumDAO.getAlbumById(1);

            assertNull(album);
            mockedDatabaseManager.verify(DatabaseManager::connect, times(1));
            verifyNoInteractions(mockArtisteDAO);
        }
    }

    @Test
    void GivenArtisteDAOReturnsArtiste_WhenGetAlbumByIdCalled_ThenReturnAlbumWithArtiste() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);
            Artiste mockArtiste = new Artiste(1, "TestArtiste");

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("SELECT * FROM albums WHERE id = ?")).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getString("titre")).thenReturn("AlbumTitle");
            when(mockResultSet.getInt("artiste_id")).thenReturn(1);
            when(mockArtisteDAO.getArtisteById(1)).thenReturn(mockArtiste);

            AlbumDAO albumDAO = new AlbumDAO();
            albumDAO.setDependance(mockArtisteDAO);
            Album album = albumDAO.getAlbumById(1);

            assertNotNull(album);
            assertEquals(1, album.getId());
            assertEquals("AlbumTitle", album.getNom());
            assertEquals(mockArtiste, album.getArtiste());

            verify(mockArtisteDAO, times(1)).getArtisteById(1);
        }
    }

    @Test
    void GivenNoAlbumsExistForArtiste_WhenGetAlbumsByArtisteCalled_ThenReturnEmptyList() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("SELECT * FROM albums WHERE artiste_id = ?")).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            AlbumDAO albumDAO = new AlbumDAO();
            List<Album> albums = albumDAO.getAlbumsByArtiste(1);

            assertNotNull(albums);
            assertTrue(albums.isEmpty());

            verify(mockPreparedStatement, times(1)).setInt(1, 1);
            verify(mockResultSet, times(1)).next();
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
            verify(mockResultSet, times(1)).close();
        }
    }

    @Test
    void GivenSQLException_WhenGetAlbumsByArtisteCalled_ThenReturnEmptyList() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabaseManager = mockStatic(DatabaseManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            mockedDatabaseManager.when(DatabaseManager::connect).thenReturn(mockConnection);
            when(mockConnection.prepareStatement("SELECT * FROM albums WHERE artiste_id = ?")).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Query failed"));

            AlbumDAO albumDAO = new AlbumDAO();
            List<Album> albums = albumDAO.getAlbumsByArtiste(1);

            assertNotNull(albums);
            assertTrue(albums.isEmpty());

            verify(mockPreparedStatement, times(1)).setInt(1, 1);
            verify(mockPreparedStatement, times(1)).executeQuery();
            verify(mockConnection, times(1)).close();
            verify(mockPreparedStatement, times(1)).close();
        }
    }
}
