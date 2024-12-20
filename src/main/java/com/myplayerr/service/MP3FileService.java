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
    private ArtisteDAO _artisteDAO;
    private AudDService _audDService;
    private ImageService _imageService;
    private AlbumDAO _albumDAO;

    public void setDependance(ChansonDAO chansonDAO, ArtisteDAO artisteDAO, AudDService audDService, ImageService imageService, AlbumDAO albumDAO) {
        _chansonDAO = chansonDAO;
        _artisteDAO = artisteDAO;
        _audDService = audDService;
        _imageService = imageService;
        _albumDAO = albumDAO;
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

            Artiste artiste = _artisteDAO.getArtisteByName(artistName);
            if (artiste == null) {
                _artisteDAO.addArtiste(artistName);
            }

            artiste = _artisteDAO.getArtisteByName(artistName);

            Album album = _albumDAO.getAlbumByNameAndArtist(albumName, artiste.getId());
            if (album == null) {
                _albumDAO.addAlbum(albumName, artiste.getId(), albumName + ".png");
                album = _albumDAO.getAlbumByNameAndArtist(albumName, artiste.getId());
                if (album != null) {
                    String albumImageUrl = _imageService.fetchAlbumImageUrl(albumName, artistName);
                    if (albumImageUrl != null) {
                        String albumImagePath = saveImage(albumName, "albums", albumImageUrl);
                        if (albumImagePath != null) {
                            _albumDAO.updateAlbumImage(album.getId(), albumImagePath);
                            _artisteDAO.updateArtisteImage(artiste.getId(), albumImagePath);
                        }
                    }
                }
            }

            _chansonDAO.addChanson(absolutePath, fileName, titleName, artiste.getId(), album.getId(), duration);
        }
    }

    private String saveImage(String name, String type, String imageUrl) {
        String saveRelativePath = "images/" + type + "/";
        String imagesDir = "./images/" + type + "/";
        File dir = new File(imagesDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileExtension = getFileExtension(imageUrl);
        String sanitizedName = name.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String fileName = sanitizedName + "." + fileExtension;
        String savePath = imagesDir + fileName;

        boolean success = _imageService.downloadImage(imageUrl, savePath);
        if (success) {
            return saveRelativePath + fileName;
        }
        return null;
    }

    private String getFileExtension(String url) {
        String extension = "jpg";
        int lastDot = url.lastIndexOf('.');
        if (lastDot != -1 && lastDot < url.length() - 1) {
            extension = url.substring(lastDot + 1).split("\\?")[0];
            if (extension.length() > 4) {
                extension = "jpg";
            }
        }
        return extension;
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
