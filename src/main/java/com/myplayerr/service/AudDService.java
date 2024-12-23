package com.myplayerr.service;

import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AudDService {

    private static final String API_URL = "https://api.audd.io/";
    private final String apiKey;

    public AudDService(){
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Fichier config.properties introuvable dans le classpath.");
            }
            props.load(input);
            apiKey = props.getProperty("api.audD.token");
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de config.properties", e);
        }
    }

    public Metadata searchSongByFile(String filePath) {
        File audioFile = new File(filePath);

        if (!audioFile.exists()) {
            System.out.println("File not found: " + filePath);
            return new Metadata("Unknown Title", "Unknown Artist", "Unknown Album");
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", apiKey)
                .addFormDataPart("file", audioFile.getName(),
                        RequestBody.create(audioFile, MediaType.parse("audio/webm")))
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);

                if (json.has("result")) {
                    JSONObject result = json.getJSONObject("result");

                    String title = result.optString("title", "Unknown Title");
                    String artist = result.optString("artist", "Unknown Artist");
                    String album = result.optString("album", "Unknown Album");

                    return new Metadata(title, artist, album);
                }
            } else {
                System.out.println("API request failed. Code: " + response.code());
                System.out.println("Message: " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Metadata("Unknown Title", "Unknown Artist", "Unknown Album");
    }

    public record Metadata(String title, String artist, String album) {
    }
}
