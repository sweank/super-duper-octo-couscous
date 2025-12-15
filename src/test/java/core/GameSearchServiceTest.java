package core;

import org.junit.jupiter.api.Test;
import stubs.StubGameDataProvider;
import static org.junit.jupiter.api.Assertions.*;

class GameSearchServiceTest {

    @Test
    void testGetGameInfo() {
        StubGameDataProvider stubProvider = new StubGameDataProvider();
        GameSearchService service = new GameSearchService(stubProvider, null);

        String result = service.getGameInfo(1L, 555);

        assertTrue(result.contains("Игра: Stub Game"));
        assertTrue(result.contains("AppID: 555"));
    }

    @Test
    void testSearchGame() {
        StubGameDataProvider stubProvider = new StubGameDataProvider();
        GameSearchService service = new GameSearchService(stubProvider, null);

        String result = service.searchGame(1L, "Test");
        assertEquals("Stub Result for Test", result);
    }

    @Test
    void testHistoryWithoutDB() {
        StubGameDataProvider stubProvider = new StubGameDataProvider();
        GameSearchService service = new GameSearchService(stubProvider, null);

        String result = service.getSearchHistory(1L, "");
        assertEquals("База данных недоступна.", result);
    }
}