package com.myplayerr.service;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Properties;

public class ImageService {

    private static final String LASTFM_API_URL = "http://ws.audioscrobbler.com/2.0/";
    private final String apiKey;
    private final OkHttpClient client;

    public ImageService(){
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Fichier config.properties introuvable dans le classpath.");
            }
            props.load(input);
            apiKey = props.getProperty("api.lastfm.key");
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de config.properties", e);
        }
        client = new OkHttpClient();
    }

    public String fetchAlbumImageUrl(String albumName, String artistName) {
        HttpUrl url = HttpUrl.parse(LASTFM_API_URL).newBuilder()
                .addQueryParameter("method", "album.getinfo")
                .addQueryParameter("album", albumName)
                .addQueryParameter("artist", artistName)
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("format", "json")
                .build();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String respBody = response.body().string();
                JSONObject json = new JSONObject(respBody);
                if (json.has("album")) {
                    JSONObject album = json.getJSONObject("album");
                    if (album.has("image")) {
                        JSONArray images = album.getJSONArray("image");
                        for (int i = images.length() - 1; i >=0; i--) {
                            JSONObject imgObj = images.getJSONObject(i);
                            String urlStr = imgObj.optString("#text");
                            if (!urlStr.isEmpty()) {
                                return urlStr;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean downloadImage(String imageUrl, String savePath) {
        Request request = new Request.Builder().url(imageUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[8192];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
