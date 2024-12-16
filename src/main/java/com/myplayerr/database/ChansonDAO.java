package com.myplayerr.database;

import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;
import com.myplayerr.model.Chanson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChansonDAO {

    public void addChanson(String path, String filename, String title, int artisteId, int albumId, String duree) {
        String sql = "INSERT INTO chansons (path, filename, title, artist_id, album_id, duree) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            pstmt.setString(2, filename);
            pstmt.setString(3, title);
            pstmt.setInt(4, artisteId);
            pstmt.setInt(5, albumId);
            pstmt.setString(6, duree);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean chansonExists(String path) {
        String sql = "SELECT 1 FROM chansons WHERE path = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Chanson> getAllChansons() {
        List<Chanson> chansons = new ArrayList<>();
        String sql = "SELECT chansons.id, chansons.title, chansons.duree, chansons.chemin_fichier, " +
                "albums.id AS album_id, albums.nom AS album_nom, artistes.id AS artiste_id, artistes.nom AS artiste_nom " +
                "FROM chansons " +
                "JOIN albums ON chansons.album_id = albums.id " +
                "JOIN artistes ON chansons.artist_id = artistes.id";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Artiste artiste = new Artiste(rs.getInt("artiste_id"), rs.getString("artiste_nom"));
                Album album = new Album(rs.getInt("album_id"), rs.getString("album_nom"), artiste);
                chansons.add(new Chanson(rs.getInt("id"), rs.getString("title"), album, artiste, rs.getString("duree"), rs.getString("chemin_fichier")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chansons;
    }

}
