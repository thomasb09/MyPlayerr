package com.myplayerr.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:myplayerr.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            String createArtistesTable = "CREATE TABLE IF NOT EXISTS artistes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL" +
                    ");";

            String createAlbumsTable = "CREATE TABLE IF NOT EXISTS albums (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "titre TEXT NOT NULL, " +
                    "artiste_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(artiste_id) REFERENCES artistes(id)" +
                    ");";

            String createChansonsTable = "CREATE TABLE IF NOT EXISTS chansons (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "path TEXT NOT NULL UNIQUE, " +
                    "filename TEXT NOT NULL, " +
                    "title TEXT, " +
                    "artist TEXT, " +
                    "album TEXT, " +
                    "duree TEXT" +
                    ");";

            String createPlaylistsTable = "CREATE TABLE IF NOT EXISTS playlists (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL" +
                    ");";

            String createPlaylistChansonsTable = "CREATE TABLE IF NOT EXISTS playlist_chansons (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "playlist_id INTEGER NOT NULL, " +
                    "chanson_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(playlist_id) REFERENCES playlists(id), " +
                    "FOREIGN KEY(chanson_id) REFERENCES chansons(id)" +
                    ");";

            String createSettingsTable = "CREATE TABLE IF NOT EXISTS settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "key TEXT NOT NULL UNIQUE, " +
                    "value TEXT NOT NULL" +
                    ");";

            stmt.execute(createSettingsTable);
            stmt.execute(createArtistesTable);
            stmt.execute(createAlbumsTable);
            stmt.execute(createChansonsTable);
            stmt.execute(createPlaylistsTable);
            stmt.execute(createPlaylistChansonsTable);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
