package com.myplayerr.database;

import com.myplayerr.model.Artiste;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour gérer les opérations liées aux artistes dans la base de données.
 */
public class ArtisteDAO {

    /**
     * Récupère un artiste par son nom.
     *
     * @param nom Nom de l'artiste.
     * @return L'objet Artiste ou null si non trouvé.
     */
    public Artiste getArtisteByName(String nom) {
        String sql = "SELECT * FROM artistes WHERE nom = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Artiste(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("image_path")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ajoute un artiste dans la base de données.
     *
     * @param nom       Nom de l'artiste.
     * @param imagePath Chemin de l'image de l'artiste.
     * @return L'ID de l'artiste ajouté ou -1 en cas d'échec.
     */
    public int addArtiste(String nom, String imagePath) {
        String sql = "INSERT INTO artistes (nom, image_path) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, imagePath);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indiquer l'échec
    }

    /**
     * Ajoute un artiste sans imagePath et retourne l'ID.
     *
     * @param nom Nom de l'artiste.
     * @return L'ID de l'artiste ajouté ou -1 en cas d'échec.
     */
    public int addArtiste(String nom) {
        return addArtiste(nom, null);
    }

    /**
     * Met à jour le chemin de l'image d'un artiste.
     *
     * @param id        ID de l'artiste.
     * @param imagePath Nouveau chemin de l'image.
     */
    public void updateArtisteImage(int id, String imagePath) {
        String sql = "UPDATE artistes SET image_path = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imagePath);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère un artiste par son ID.
     *
     * @param artisteId ID de l'artiste.
     * @return L'objet Artiste ou null si non trouvé.
     */
    public Artiste getArtisteById(int artisteId) {
        String sql = "SELECT * FROM artistes WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, artisteId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Artiste(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("image_path")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère tous les artistes.
     *
     * @return Liste de tous les artistes.
     */
    public List<Artiste> getAllArtistes() {
        List<Artiste> artistes = new ArrayList<>();
        String sql = "SELECT * FROM artistes";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                artistes.add(new Artiste(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistes;
    }
}
