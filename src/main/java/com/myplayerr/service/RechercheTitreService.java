package com.myplayerr.service;

import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RechercheTitreService {
    private String apiKey;
    private static final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";

    public RechercheTitreService() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Fichier config.properties introuvable dans le classpath.");
            }
            props.load(input);
            apiKey = props.getProperty("api.youtube.token");
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de config.properties", e);
        }
    }

    public Task<List<List<String>>> rechercheYouTubeAsync(String query) {
        return new Task<>() {
            @Override
            protected List<List<String>> call() throws Exception {
                return rechercheYouTube(query);
            }
        };
    }

    private List<List<String>> rechercheYouTube(String query) {
        try {
            String urlString = SEARCH_URL + "?part=snippet&q=" + query.replace(" ", "+") +
                    "&type=video&maxResults=10&key=" + apiKey;
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            String jsonResponse = response.toString();
            return extractVideoDetails(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<List<String>> extractVideoDetails(String jsonResponse) {
        List<List<String>> videoDetails = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < Math.min(items.length(), 10); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");

                String title = snippet.getString("title");
                String author = snippet.getString("channelTitle");
                String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("default").getString("url");
                String videoId = item.getJSONObject("id").getString("videoId");
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

                List<String> videoInfo = new ArrayList<>();
                videoInfo.add(title + ", de : " + author);
                videoInfo.add(thumbnailUrl);
                videoInfo.add(videoUrl);

                videoDetails.add(videoInfo);
            }

            if (videoDetails.isEmpty()) {
                List<String> noResult = new ArrayList<>();
                noResult.add("Aucune vidéo trouvée.");
                noResult.add("");
                videoDetails.add(noResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
            List<String> errorResult = new ArrayList<>();
            errorResult.add("Erreur lors de l'extraction des détails des vidéos.");
            errorResult.add("");
            videoDetails.add(errorResult);
        }
        return videoDetails;
    }
}
