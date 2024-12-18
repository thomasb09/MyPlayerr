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

    public Album getAlbumByNameAndArtist(String nom, int artisteId) {
        String sql = "SELECT * FROM albums WHERE nom = ? AND artiste_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setInt(2, artisteId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Album(rs.getInt("id"), rs.getString("nom"), null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAlbum(String nom, int artisteId) {
        String sql = "INSERT OR IGNORE INTO albums (nom, artiste_id) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setInt(2, artisteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT albums.id, albums.nom, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums " +
                "JOIN artistes ON albums.artiste_id = artistes.id";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Artiste artiste = new Artiste(rs.getInt("artiste_id"), rs.getString("artiste_nom"));
                albums.add(new Album(rs.getInt("id"), rs.getString("nom"), artiste));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public Album getAlbumById(int albumId) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM albums WHERE id = ?")) {

            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Artiste artiste = _artisteDAO.getArtisteById(rs.getInt("artiste_id"));

                return new Album(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        artiste
                );
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
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                albums.add(new Album(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        new Artiste(artisteId, "")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }
}
