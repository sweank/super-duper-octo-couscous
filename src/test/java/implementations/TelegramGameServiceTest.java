package implementations;


import models.GameInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TelegramGameServiceTest {

    private static class StubSteamApiClient extends SteamApiClient {
        @Override
        public GameInfo getGameInfo(int appId) {
            return new GameInfo(
                    "Test Game", appId, 100.0, 100.0, "RUB", 0,
                    false, "http://image.url", "Desc", null, null, null, null
            );
        }
    }

    @Test
    void testGetGameInfoWithImage() throws Exception {
        StubSteamApiClient stubClient = new StubSteamApiClient();
        TelegramGameService service = new TelegramGameService(stubClient);

        String result = service.getGameInfoWithImage(123);

        assertTrue(result.contains("IMAGE_URL:http://image.url"));
        assertTrue(result.contains("*Test Game*"));
    }

    @Test
    void testGetGameInfoForTelegram() throws Exception {
        StubSteamApiClient stubClient = new StubSteamApiClient();
        TelegramGameService service = new TelegramGameService(stubClient);

        String result = service.getGameInfoForTelegram(123);

        assertFalse(result.contains("IMAGE_URL:"));
        assertTrue(result.contains("*Test Game*"));
    }
}