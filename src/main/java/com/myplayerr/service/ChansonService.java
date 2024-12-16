package com.myplayerr.service;

import com.mpatric.mp3agic.*;
import com.myplayerr.database.ChansonDAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ChansonService {

    private final ChansonDAO chansonDAO = new ChansonDAO();
    private final AudDService audDService = new AudDService();

    public void scanAndImportMusic(String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".mp3"))
                    .forEach(this::processFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processFile(Path filePath) {
        String absolutePath = filePath.toAbsolutePath().toString();
        String fileName = filePath.getFileName().toString();

        if (chansonDAO.chansonExists(absolutePath)) {
            String title = "Unknown Title";
            String artist = "Unknown Artist";
            String album = "Unknown Album";
            String duration = "0:00";

            try {
                Mp3File mp3File = new Mp3File(absolutePath);
                duration = formatDuration(mp3File.getLengthInSeconds());
            } catch (Exception ignored) {

            }

            AudDService.Metadata metadata = audDService.searchSongByFile(absolutePath);
            title = metadata.title();
            artist = metadata.artist();
            album = metadata.album();

            chansonDAO.addChanson(absolutePath, fileName, title, artist, album, duration);
        }
    }


    private String formatDuration(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
}
