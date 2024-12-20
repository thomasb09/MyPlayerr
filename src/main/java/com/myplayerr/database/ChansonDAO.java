package com.myplayerr.database;

import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;
import com.myplayerr.model.Chanson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour gérer les opérations liées aux chansons dans la base de données.
 */
public class ChansonDAO {

    private AlbumDAO _albumDAO;

    /**
     * Définit la dépendance à AlbumDAO.
     *
     * @param albumDAO Instance de AlbumDAO.
     */
    public void setDependance(AlbumDAO albumDAO) {
        _albumDAO = albumDAO;
    }

    /**
     * Récupère toutes les chansons d'un album spécifique.
     *
     * @param albumId ID de l'album.
     * @return Liste de toutes les chansons de l'album.
     */
    public List<Chanson> getChansonByAlbum(int albumId) {
        List<Chanson> chansons = new ArrayList<>();
        String sql = "SELECT chansons.id, chansons.title, chansons.duree, chansons.path, " +
                "albums.id AS album_id, albums.titre AS album_titre, albums.image_path AS album_image_path, " +
                "artistes.id AS artiste_id, artistes.nom AS artiste_nom, artistes.image_path AS artiste_image_path " +
                "FROM chansons " +
                "JOIN albums ON chansons.album_id = albums.id " +
                "JOIN artistes ON chansons.artist_id = artistes.id " +
                "WHERE albums.id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, albumId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Artiste artiste = new Artiste(
                            rs.getInt("artiste_id"),
                            rs.getString("artiste_nom"),
                            rs.getString("artiste_image_path")
                    );
                    Album album = new Album(
                            rs.getInt("album_id"),
                            rs.getString("album_titre"),
                            artiste,
                            rs.getString("album_image_path")
                    );
                    Chanson chanson = new Chanson(
                            rs.getInt("id"),
                            rs.getString("title"),
                            album,
                            artiste,
                            rs.getString("duree"),
                            rs.getString("path")
                    );
                    chansons.add(chanson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chansons;
    }

    /**
     * Ajoute une chanson dans la base de données.
     *
     * @param path      Chemin du fichier.
     * @param filename  Nom du fichier.
     * @param title     Titre de la chanson.
     * @param artisteId ID de l'artiste.
     * @param albumId   ID de l'album.
     * @param duree     Durée de la chanson.
     */
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

    /**
     * Vérifie si une chanson existe déjà dans la base de données.
     *
     * @param path Chemin du fichier.
     * @return true si la chanson existe, false sinon.
     */
    public boolean chansonExists(String path) {
        String sql = "SELECT 1 FROM chansons WHERE path = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère toutes les chansons de la base de données.
     *
     * @return Liste de toutes les chansons.
     */
    public List<Chanson> getAllChansons() {
        List<Chanson> chansons = new ArrayList<>();
        String sql = "SELECT chansons.id, chansons.title, chansons.duree, chansons.path, " +
                "albums.id AS album_id, albums.titre AS album_titre, albums.image_path AS album_image_path, " +
                "artistes.id AS artiste_id, artistes.nom AS artiste_nom, artistes.image_path AS artiste_image_path " +
                "FROM chansons " +
                "JOIN albums ON chansons.album_id = albums.id " +
                "JOIN artistes ON chansons.artist_id = artistes.id";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Artiste artiste = new Artiste(
                        rs.getInt("artiste_id"),
                        rs.getString("artiste_nom"),
                        rs.getString("artiste_image_path")
                );
                Album album = new Album(
                        rs.getInt("album_id"),
                        rs.getString("album_titre"),
                        artiste,
                        rs.getString("album_image_path")
                );
                Chanson chanson = new Chanson(
                        rs.getInt("id"),
                        rs.getString("title"),
                        album,
                        artiste,
                        rs.getString("duree"),
                        rs.getString("path")
                );
                chansons.add(chanson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chansons;
    }

    /**
     * Récupère une chanson par son ID.
     *
     * @param chansonId ID de la chanson.
     * @return L'objet Chanson ou null si non trouvé.
     */
    public Chanson getChansonById(int chansonId) {
        String sql = "SELECT chansons.id, chansons.title, chansons.duree, chansons.path, " +
                "albums.id AS album_id, albums.titre AS album_titre, albums.image_path AS album_image_path, " +
                "artistes.id AS artiste_id, artistes.nom AS artiste_nom, artistes.image_path AS artiste_image_path " +
                "FROM chansons " +
                "JOIN albums ON chansons.album_id = albums.id " +
                "JOIN artistes ON chansons.artist_id = artistes.id " +
                "WHERE chansons.id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, chansonId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Artiste artiste = new Artiste(
                            rs.getInt("artiste_id"),
                            rs.getString("artiste_nom"),
                            rs.getString("artiste_image_path")
                    );
                    Album album = new Album(
                            rs.getInt("album_id"),
                            rs.getString("album_titre"),
                            artiste,
                            rs.getString("album_image_path")
                    );
                    return new Chanson(
                            rs.getInt("id"),
                            rs.getString("title"),
                            album,
                            artiste,
                            rs.getString("duree"),
                            rs.getString("path")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
