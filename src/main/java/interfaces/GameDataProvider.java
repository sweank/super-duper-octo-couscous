package interfaces;

import models.GameInfo;

public interface GameDataProvider {
    GameInfo getGameInfo(int appId) throws Exception;
    String searchGame(String gameName) throws Exception;
}