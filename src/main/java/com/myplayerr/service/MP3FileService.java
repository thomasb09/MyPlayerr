package com.myplayerr.service;

import com.mpatric.mp3agic.*;
import com.myplayerr.database.AlbumDAO;
import com.myplayerr.database.ArtisteDAO;
import com.myplayerr.database.ChansonDAO;
import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MP3FileService {

    private ChansonDAO _chansonDAO;
    private AudDService _audDService;

    public void setDependance(ChansonDAO chansonDAO, AudDService audDService) {
        _chansonDAO = chansonDAO;
        _audDService = audDService;
    }

    public void scanAndImportMusic(String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".mp3") || p.toString().toLowerCase().endsWith(".webm"))
                    .forEach(this::processFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processFile(Path filePath) {
        String absolutePath = filePath.toAbsolutePath().toString();
        String fileName = filePath.getFileName().toString();

        if (!_chansonDAO.chansonExists(absolutePath)) {
            String titleName;
            String artistName;
            String albumName;
            String duration = "0:00";

            try {
                Mp3File mp3File = new Mp3File(absolutePath);
                duration = formatDuration(mp3File.getLengthInSeconds());
            } catch (Exception ignored) {

            }

            AudDService.Metadata metadata = _audDService.searchSongByFile(absolutePath);

            titleName = metadata.title();
            artistName = metadata.artist();
            albumName = metadata.album();

            try {
                updateFileMetadata(absolutePath, titleName, artistName, albumName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            ArtisteDAO artisteDAO = new ArtisteDAO();
            Artiste artiste = artisteDAO.getArtisteByName(artistName);
            if (artiste == null) {
                artisteDAO.addArtiste(artistName);
                artiste = artisteDAO.getArtisteByName(artistName);
            }

            AlbumDAO albumDAO = new AlbumDAO();
            Album album = albumDAO.getAlbumByNameAndArtist(albumName, artiste.getId());
            if (album == null) {
                albumDAO.addAlbum(albumName, artiste.getId());
                album = albumDAO.getAlbumByNameAndArtist(albumName, artiste.getId());
            }

            _chansonDAO.addChanson(absolutePath, fileName, titleName, artiste.getId(), album.getId(), duration);
        }
    }

    private void updateFileMetadata(String filePath, String title, String artist, String album)
            throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
        Mp3File mp3File = new Mp3File(filePath);

        if (mp3File.hasId3v2Tag()) {
            ID3v2 tag = mp3File.getId3v2Tag();
            tag.setTitle(title);
            tag.setArtist(artist);
            tag.setAlbum(album);
        } else {
            ID3v2 tag = new com.mpatric.mp3agic.ID3v24Tag();
            tag.setTitle(title);
            tag.setArtist(artist);
            tag.setAlbum(album);
            mp3File.setId3v2Tag(tag);
        }

        String tempFilePath = filePath + ".temp";
        mp3File.save(tempFilePath);

        File originalFile = new File(filePath);
        if (originalFile.delete()) {
            File updatedFile = new File(tempFilePath);
            updatedFile.renameTo(originalFile);
        }
    }

    private String formatDuration(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
}
