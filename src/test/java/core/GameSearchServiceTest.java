package core;

import org.junit.jupiter.api.Test;
import stubs.StubGameDataProvider;
import static org.junit.jupiter.api.Assertions.*;

class GameSearchServiceTest {

    @Test
    void testGetGameInfo() {
        StubGameDataProvider stubProvider = new StubGameDataProvider();
        GameSearchService service = new GameSearchService(stubProvider);

        String result = service.getGameInfo(555);

        assertTrue(result.contains("Игра: Stub Game"));
        assertTrue(result.contains("AppID: 555"));
    }

    @Test
    void testSearchGame() {
        StubGameDataProvider stubProvider = new StubGameDataProvider();
        GameSearchService service = new GameSearchService(stubProvider);

        String result = service.searchGame("Test");
        assertEquals("Stub Result for Test", result);
    }
}