package com.myplayerr.service;

import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class AudDService {

    private static final String API_URL = "https://api.audd.io/";
    private static final String API_KEY = "2f582b6636e756d630077de294af79d7";

    public Metadata searchSongByFile(String filePath) {
        File audioFile = new File(filePath);

        if (!audioFile.exists()) {
            System.out.println("File not found: " + filePath);
            return new Metadata("Unknown Title", "Unknown Artist", "Unknown Album");
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", API_KEY)
                .addFormDataPart("file", audioFile.getName(),
                        RequestBody.create(audioFile, MediaType.parse("audio/mpeg")))
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
