package com.myplayerr.database;

import com.myplayerr.model.Chanson;
import com.myplayerr.model.Playlist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    private ChansonDAO _chansonDAO;

    public void setDependance(ChansonDAO chansonDAO) {
        _chansonDAO = chansonDAO;
    }

    public void addPlaylist(Playlist playlist) {

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO playlists (nom) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, playlist.getName());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                playlist.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM playlists")) {

            while (rs.next()) {
                Playlist playlist = new Playlist(rs.getInt("id"), rs.getString("nom"));
                playlist.getChansons().addAll(getChansonsForPlaylist(playlist.getId()));
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    public void addChansonToPlaylist(int playlistId, int chansonId) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO playlist_chansons (playlist_id, chanson_id) VALUES (?, ?)")) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, chansonId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeChansonFromPlaylist(int playlistId, int chansonId) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM playlist_chansons WHERE playlist_id = ? AND chanson_id = ?")) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, chansonId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Chanson> getChansonsForPlaylist(int playlistId) {
        List<Chanson> chansons = new ArrayList<>();
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.* FROM chansons c " +
                             "JOIN playlist_chansons pc ON c.id = pc.chanson_id " +
                             "WHERE pc.playlist_id = ?")) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                chansons.add(_chansonDAO.getChansonById(rs.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chansons;
    }
}
