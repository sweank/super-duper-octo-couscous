package implementations;

import interfaces.GameDataProvider;
import models.GameInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SteamApiClient implements GameDataProvider {
    private static final int MIN_SEARCH_LENGTH = 2;
    private static final int MAX_SEARCH_RESULTS = 5;
    private static final int CONNECT_TIMEOUT_MS = 10000;
    private static final int READ_TIMEOUT_MS = 10000;
    private static final int HTTP_SUCCESS_CODE = 200;
    private static final int PRICE_DIVIDER = 100;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public GameInfo getGameInfo(int appId) throws Exception {
        String appDetailsUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&l=russian";
        String response = sendHttpGetRequest(appDetailsUrl);

        JsonNode jsonResponse = mapper.readTree(response);
        JsonNode gameData = jsonResponse.get(String.valueOf(appId));

        if (gameData == null || !gameData.get("success").asBoolean()) {
            throw new Exception("Игра не найдена. Проверьте AppID.");
        }

        JsonNode data = gameData.get("data");
        return parseGameInfo(data);
    }

    @Override
    public String searchGame(String gameName) throws Exception {
        if (gameName.length() < MIN_SEARCH_LENGTH) {
            return "Введите минимум " + MIN_SEARCH_LENGTH + " символа для поиска.";
        }

        String encodedName = URLEncoder.encode(gameName, StandardCharsets.UTF_8.toString());
        String searchUrl = "https://store.steampowered.com/api/storesearch/?term=" + encodedName + "&l=russian&cc=RU";

        String response = sendHttpGetRequest(searchUrl);
        JsonNode jsonResponse = mapper.readTree(response);

        List<String> foundGames = new ArrayList<>();

        if (jsonResponse.has("items")) {
            JsonNode items = jsonResponse.get("items");

            for (JsonNode item : items) {
                String name = item.get("name").asText();
                int appId = item.get("id").asInt();

                foundGames.add(name + " (AppID: " + appId + ")");
                if (foundGames.size() >= MAX_SEARCH_RESULTS) break;
            }
        }

        if (foundGames.isEmpty()) {
            return "Игра '" + gameName + "' не найдена.\n" +
                    "Попробуйте другое название или используйте команду: info <AppID>";
        }

        StringBuilder result = new StringBuilder("Найдены игры:\n");
        for (String game : foundGames) {
            result.append(game).append("\n");
        }
        result.append("\nИспользуйте команду: info <AppID> для подробной информации");
        return result.toString();
    }

    public GameInfo parseGameInfo(JsonNode data) {
        String name = data.get("name").asText();
        int appId = data.get("steam_appid").asInt();

        String imageUrl = data.has("header_image") ?
                data.get("header_image").asText() : null;

        String description = data.has("short_description") ?
                data.get("short_description").asText() : "";

        String releaseDate = "";
        if (data.has("release_date") && data.get("release_date").has("date")) {
            releaseDate = data.get("release_date").get("date").asText();
        }

        String developers = "";
        if (data.has("developers")) {
            List<String> devList = new ArrayList<>();
            for (JsonNode dev : data.get("developers")) {
                devList.add(dev.asText());
            }
            developers = String.join(", ", devList);
        }

        String publishers = "";
        if (data.has("publishers")) {
            List<String> pubList = new ArrayList<>();
            for (JsonNode pub : data.get("publishers")) {
                pubList.add(pub.asText());
            }
            publishers = String.join(", ", pubList);
        }

        String[] categories = new String[0];
        if (data.has("categories")) {
            List<String> catList = new ArrayList<>();
            for (JsonNode cat : data.get("categories")) {
                catList.add(cat.get("description").asText());
            }
            categories = catList.toArray(new String[0]);
        }

        boolean isFree = data.has("is_free") && data.get("is_free").asBoolean();
        Double finalPrice = null;
        Double originalPrice = null;
        String currency = null;
        Integer discountPercent = null;

        if (!isFree && data.has("price_overview")) {
            JsonNode price = data.get("price_overview");
            finalPrice = price.get("final").asInt() / (double) PRICE_DIVIDER;
            originalPrice = price.get("initial").asInt() / (double) PRICE_DIVIDER;
            currency = price.get("currency").asText();
            discountPercent = price.has("discount_percent") ?
                    price.get("discount_percent").asInt() : null;

            if (discountPercent == null && originalPrice > 0 && finalPrice < originalPrice) {
                discountPercent = (int) ((1 - finalPrice / originalPrice) * 100);
            }
        }

        return new GameInfo(name, appId, finalPrice, originalPrice,
                currency, discountPercent, isFree, imageUrl,
                description, releaseDate, developers,
                publishers, categories);
    }

    public String sendHttpGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HTTP_SUCCESS_CODE) {
            throw new Exception("HTTP ошибка: " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}