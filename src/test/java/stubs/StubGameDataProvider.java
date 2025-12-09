package stubs;

import interfaces.GameDataProvider;
import models.GameInfo;

public class StubGameDataProvider implements GameDataProvider {
    @Override
    public GameInfo getGameInfo(int appId) {
        return new GameInfo("Stub Game", appId, 100.0, 100.0, "RUB", 0, false, null, null, null, null, null, null);
    }

    @Override
    public String searchGame(String gameName) {
        return "Stub Result for " + gameName;
    }
}