package com.myplayerr.service;

import com.myplayerr.database.SettingDAO;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class DownloadChansonSevice {

    private MP3FileService _mp3FileService;
    private FFmpegConvertisseurService _fFmpegConvertisseurService;
    private String musicPath = "";

    public DownloadChansonSevice(){
    }

    public void setDependance(SettingDAO settingDAO, MP3FileService mp3FileService, FFmpegConvertisseurService fFmpegConvertisseurService){
        _mp3FileService = mp3FileService;
        _fFmpegConvertisseurService = fFmpegConvertisseurService;
        musicPath = settingDAO.getSetting("mp3Path");
    }

    public void download(String videoUrl) throws IOException, InterruptedException {
        String outputTemplate = musicPath + "/%(title)s.%(ext)s";

        ProcessBuilder pb = new ProcessBuilder(
                "C:\\MyPlayerr\\yt-dlp.exe",
                "--extract-audio",
                "--audio-format", "mp3",
                "--output", outputTemplate,
                videoUrl
        );

        pb.inheritIO();

        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            _mp3FileService.scanAndImportMusic(musicPath);
            return;
        }

        File downloadedFile = getDownloadedFile();
        if (downloadedFile == null) {
            return;
        }

        if (!downloadedFile.getName().toLowerCase().endsWith(".mp3")) {
            String mp3Path = replaceExtension(downloadedFile.getAbsolutePath(), ".mp3");
            _fFmpegConvertisseurService.convertWebMToMp3(downloadedFile.getAbsolutePath(), mp3Path);

            downloadedFile.delete();
        }
        _mp3FileService.scanAndImportMusic(musicPath);
    }

    private File getDownloadedFile() {

        File dir = new File(musicPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".webm"));
        if (files == null || files.length == 0) {
            return null;
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        return files[0];
    }

    private String replaceExtension(String filePath, String newExtension) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1) {
            return filePath + newExtension;
        }
        return filePath.substring(0, lastDot) + newExtension;
    }

}
