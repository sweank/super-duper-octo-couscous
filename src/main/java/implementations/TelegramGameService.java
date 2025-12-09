package implementations;

import models.GameInfo;

public class TelegramGameService {
    private final SteamApiClient steamApiClient;

    public TelegramGameService(SteamApiClient steamApiClient) {
        this.steamApiClient = steamApiClient;
    }

    public String getGameInfoWithImage(int appId) throws Exception {
        GameInfo gameInfo = steamApiClient.getGameInfo(appId);

        String imageUrl = gameInfo.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            return gameInfo.formatForTelegram() + "\n\n \nIMAGE_URL:" + imageUrl;
        } else {
            return gameInfo.formatForTelegram() + "\n\n*Изображение не найдено*";
        }
    }

    public String getGameInfoForTelegram(int appId) throws Exception {
        GameInfo gameInfo = steamApiClient.getGameInfo(appId);
        return gameInfo.formatForTelegram();
    }
}