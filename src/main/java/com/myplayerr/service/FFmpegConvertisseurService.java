package com.myplayerr.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFmpegConvertisseurService {

    public void convertWebMToMp3(String inputPath, String outputPath) {
        String command = String.format("C:\\MyPlayerr\\ffmpeg-7.0.2-essentials_build\\bin\\ffmpeg.exe -i \"%s\" \"%s\"", inputPath, outputPath);

        try {
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Échec de la conversion. Code de sortie : " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
