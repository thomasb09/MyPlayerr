package com.myplayerr.database;

import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour gérer les opérations liées aux albums dans la base de données.
 */
public class AlbumDAO {

    private ArtisteDAO _artisteDAO;

    /**
     * Définit la dépendance à ArtisteDAO.
     *
     * @param artisteDAO Instance de ArtisteDAO.
     */
    public void setDependance(ArtisteDAO artisteDAO) {
        _artisteDAO = artisteDAO;
    }

    /**
     * Ajoute un album dans la base de données.
     *
     * @param titre      Titre de l'album.
     * @param artisteId  ID de l'artiste.
     * @param imagePath  Chemin de l'image de l'album.
     * @return L'ID de l'album ajouté ou -1 en cas d'échec.
     */
    public int addAlbum(String titre, int artisteId, String imagePath) {
        String sql = "INSERT INTO albums (titre, artiste_id, image_path) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, titre);
            pstmt.setInt(2, artisteId);
            pstmt.setString(3, imagePath);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Retourner l'ID généré
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indiquer l'échec
    }

    /**
     * Ajoute un album sans imagePath et retourne l'ID.
     *
     * @param titre      Titre de l'album.
     * @param artisteId  ID de l'artiste.
     * @return L'ID de l'album ajouté ou -1 en cas d'échec.
     */
    public int addAlbum(String titre, int artisteId) {
        return addAlbum(titre, artisteId, null);
    }

    /**
     * Récupère un album par son nom et l'ID de l'artiste.
     *
     * @param nom        Nom de l'album.
     * @param artisteId  ID de l'artiste.
     * @return L'objet Album ou null si non trouvé.
     */
    public Album getAlbumByNameAndArtist(String nom, int artisteId) {
        String sql = "SELECT * FROM albums WHERE titre = ? AND artiste_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setInt(2, artisteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String titre = rs.getString("titre");
                    String imagePath = rs.getString("image_path");
                    Artiste artiste = _artisteDAO.getArtisteById(artisteId);
                    return new Album(id, titre, artiste, imagePath);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère un album par son ID.
     *
     * @param albumId ID de l'album.
     * @return L'objet Album ou null si non trouvé.
     */
    public Album getAlbumById(int albumId) {
        String sql = "SELECT * FROM albums WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, albumId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int artisteId = rs.getInt("artiste_id");
                    Artiste artiste = _artisteDAO.getArtisteById(artisteId);
                    String titre = rs.getString("titre");
                    String imagePath = rs.getString("image_path");
                    return new Album(albumId, titre, artiste, imagePath);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère tous les albums avec les informations des artistes.
     *
     * @return Liste de tous les albums.
     */
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT albums.id, albums.titre, albums.image_path, artistes.nom AS artiste_nom, albums.artiste_id AS artiste_id FROM albums " +
                "JOIN artistes ON albums.artiste_id = artistes.id";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String imagePath = rs.getString("image_path");
                int artisteId = rs.getInt("artiste_id");
                String artisteNom = rs.getString("artiste_nom");
                Artiste artiste = new Artiste(artisteId, artisteNom, null); // Récupérer image_path si nécessaire
                albums.add(new Album(id, titre, artiste, imagePath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    /**
     * Récupère tous les albums d'un artiste spécifique.
     *
     * @param artisteId ID de l'artiste.
     * @return Liste de tous les albums de l'artiste.
     */
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
                    String imagePath = rs.getString("image_path");
                    Artiste artiste = _artisteDAO.getArtisteById(artisteId);
                    Album album = new Album(id, titre, artiste, imagePath);
                    albums.add(album);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    /**
     * Met à jour le chemin de l'image d'un album.
     *
     * @param id        ID de l'album.
     * @param imagePath Nouveau chemin de l'image.
     */
    public void updateAlbumImage(int id, String imagePath) {
        String sql = "UPDATE albums SET image_path = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imagePath);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
