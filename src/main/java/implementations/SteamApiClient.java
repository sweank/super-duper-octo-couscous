package implementations;

import interfaces.IGameDataProvider;
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

public class SteamApiClient implements IGameDataProvider {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getGameInfo(int appId) throws Exception {
        String appDetailsUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&l=russian";
        String response = sendHttpGetRequest(appDetailsUrl);

        JsonNode jsonResponse = mapper.readTree(response);
        JsonNode gameData = jsonResponse.get(String.valueOf(appId));

        if (gameData == null || !gameData.get("success").asBoolean()) {
            return "Информация об игре не найдена. Проверьте AppID.";
        }

        JsonNode data = gameData.get("data");
        return formatGameInfo(data);
    }

    @Override
    public String searchGame(String gameName) throws Exception {
        if (gameName.length() < 2) {
            return "Введите минимум 2 символа для поиска.";
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
                if (foundGames.size() >= 5) break;
            }
        }

        if (foundGames.isEmpty()) {
            return "Игра '" + gameName + "' не найдена.\n" +
                    "Попробуйте другое название или используйте команду: /info <AppID>";
        }

        StringBuilder result = new StringBuilder(" Найдены игры:\n\n");
        for (String game : foundGames) {
            result.append(game).append("\n");
        }
        result.append("\n Используйте команду: `/info <AppID>` для подробной информации");
        return result.toString();
    }

    private String sendHttpGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP ошибка: " + responseCode + " для URL: " + urlString);
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

    private String formatGameInfo(JsonNode data) {
        StringBuilder info = new StringBuilder();

        info.append("Игра: ").append(data.get("name").asText()).append("\n\n");

        if (data.has("price_overview")) {
            JsonNode price = data.get("price_overview");
            double finalPrice = price.get("final").asInt() / 100.0;
            double originalPrice = price.get("initial").asInt() / 100.0;
            String currency = getCurrencySymbol(price.get("currency").asText());

            if (finalPrice == originalPrice || originalPrice == 0) {
                info.append(" Цена: ").append(currency).append(finalPrice);
            } else {
                int discount = price.get("discount_percent").asInt();
                info.append(" Цена: ").append(currency).append(finalPrice)
                        .append(" (скидка ").append(discount).append("%)");
            }
        } else {
            info.append(" Бесплатно");
        }

        info.append("\n\n https://store.steampowered.com/app/").append(data.get("steam_appid").asInt());

        return info.toString();
    }

    private String getCurrencySymbol(String currencyCode) {
        switch (currencyCode) {
            case "USD": return "$";
            case "EUR": return "€";
            case "RUB": return "₽";
            case "UAH": return "₴";
            case "KZT": return "₸";
            case "GBP": return "£";
            default: return currencyCode + " ";
        }
    }
}