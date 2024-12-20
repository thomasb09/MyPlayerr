package com.myplayerr.database;

import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    private ArtisteDAO _artisteDAO;

    public void setDependance(ArtisteDAO artisteDAO) {
        _artisteDAO = artisteDAO;
    }

    public void addAlbum(String titre, int artisteId) {
        String sql = "INSERT OR IGNORE INTO albums (titre, artiste_id) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titre);
            pstmt.setInt(2, artisteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Album getAlbumByNameAndArtist(String nom, int artisteId) {
        String sql = "SELECT * FROM albums WHERE titre = ? AND artiste_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setInt(2, artisteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Album(rs.getInt("id"), rs.getString("titre"), null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT albums.id, albums.titre, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums " +
                "JOIN artistes ON albums.artiste_id = artistes.id";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // Inclusion de ResultSet dans try-with-resources

            while (rs.next()) {
                Artiste artiste = new Artiste(rs.getInt("artiste_id"), rs.getString("artiste_nom"));
                albums.add(new Album(rs.getInt("id"), rs.getString("titre"), artiste));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public Album getAlbumById(int albumId) {
        String sql = "SELECT * FROM albums WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, albumId);

            try (ResultSet rs = stmt.executeQuery()) { // Inclusion de ResultSet dans try-with-resources
                if (rs.next()) {
                    Artiste artiste = _artisteDAO.getArtisteById(rs.getInt("artiste_id"));
                    return new Album(rs.getInt("id"), rs.getString("titre"), artiste);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Album> getAlbumsByArtiste(int artisteId) {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM albums WHERE artiste_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, artisteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titre = rs.getString("titre");
                    Artiste artiste = _artisteDAO.getArtisteById(artisteId);
                    Album album = new Album(id, titre, artiste);
                    albums.add(album);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }
}
