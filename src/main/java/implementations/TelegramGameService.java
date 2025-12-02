package implementations;


import models.GameInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TelegramGameService {
    private final SteamApiClient steamApiClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public TelegramGameService(SteamApiClient steamApiClient) {
        this.steamApiClient = steamApiClient;
    }

    public String getGameInfoWithImage(int appId) throws Exception {
        String appDetailsUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&l=russian";
        String response = steamApiClient.sendHttpGetRequest(appDetailsUrl);

        JsonNode jsonResponse = mapper.readTree(response);
        JsonNode gameData = jsonResponse.get(String.valueOf(appId));

        if (gameData == null || !gameData.get("success").asBoolean()) {
            return "Информация об игре не найдена. Проверьте AppID.";
        }

        JsonNode data = gameData.get("data");
        GameInfo gameInfo = steamApiClient.parseGameInfo(data);
        String imageUrl = data.has("header_image") ?
                data.get("header_image").asText() : null;

        if (imageUrl != null && !imageUrl.isEmpty()) {
            return gameInfo.formatForTelegram() + "\n\n \nIMAGE_URL:" + imageUrl;
        } else {
            return gameInfo.formatForTelegram() + "\n\n*Изображение не найдено*";
        }
    }

    public String getGameInfoForTelegram(int appId) throws Exception {
        String appDetailsUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&l=russian";
        String response = steamApiClient.sendHttpGetRequest(appDetailsUrl);

        JsonNode jsonResponse = mapper.readTree(response);
        JsonNode gameData = jsonResponse.get(String.valueOf(appId));

        if (gameData == null || !gameData.get("success").asBoolean()) {
            return "Информация об игре не найдена. Проверьте AppID.";
        }

        JsonNode data = gameData.get("data");
        GameInfo gameInfo = steamApiClient.parseGameInfo(data);

        return gameInfo.formatForTelegram();
    }
}