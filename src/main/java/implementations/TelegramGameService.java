package implementations;

import database.DatabaseHandler;
import models.GameInfo;

public class TelegramGameService {
    private final SteamApiClient steamApiClient;
    private final DatabaseHandler dbHandler;

    public TelegramGameService(SteamApiClient steamApiClient, DatabaseHandler dbHandler) {
        this.steamApiClient = steamApiClient;
        this.dbHandler = dbHandler;
    }

    public String getGameInfoWithImage(long userId, int appId) throws Exception {
        if (dbHandler != null) {
            dbHandler.saveSearch(userId, String.valueOf(appId), "INFO");
        }

        GameInfo gameInfo = steamApiClient.getGameInfo(appId);
        String imageUrl = gameInfo.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            return gameInfo.formatForTelegram() + "\n\n \nIMAGE_URL:" + imageUrl;
        } else {
            return gameInfo.formatForTelegram() + "\n\n*Изображение не найдено*";
        }
    }

    public String getGameInfoForTelegram(long userId, int appId) throws Exception {
        if (dbHandler != null) {
            dbHandler.saveSearch(userId, String.valueOf(appId), "INFO");
        }
        GameInfo gameInfo = steamApiClient.getGameInfo(appId);
        return gameInfo.formatForTelegram();
    }
}