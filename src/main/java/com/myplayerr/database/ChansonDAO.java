package com.myplayerr.database;

import com.myplayerr.model.Chanson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChansonDAO {

    public boolean chansonExists(String path) {
        String sql = "SELECT COUNT(*) FROM chansons WHERE path = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, path);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) <= 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void addChanson(String path, String filename, String title, String artist, String album, String duration) {
        String sql = "INSERT INTO chansons (path, filename, title, artist, album, duree) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, path);
            pstmt.setString(2, filename);
            pstmt.setString(3, title);
            pstmt.setString(4, artist);
            pstmt.setString(5, album);
            pstmt.setString(6, duration);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Chanson> getAllChansons() {
        List<Chanson> chansons = new ArrayList<>();
        String sql = "SELECT * FROM chansons";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Chanson chanson = new Chanson(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("albumId"),
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

}
