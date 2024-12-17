package com.myplayerr.database;

import com.myplayerr.model.Artiste;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtisteDAO {

    public Artiste getArtisteByName(String nom) {
        String sql = "SELECT * FROM artistes WHERE nom = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Artiste(rs.getInt("id"), rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addArtiste(String nom) {
        String sql = "INSERT OR IGNORE INTO artistes (nom) VALUES (?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Artiste> getAllArtistes() {
        List<Artiste> artistes = new ArrayList<>();
        String sql = "SELECT * FROM artistes";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                artistes.add(new Artiste(rs.getInt("id"), rs.getString("nom")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistes;
    }

    public static Artiste getArtisteById(int artisteId) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM artistes WHERE id = ?")) {

            stmt.setInt(1, artisteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Artiste(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
