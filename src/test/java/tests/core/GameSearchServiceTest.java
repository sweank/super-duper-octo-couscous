package tests.core;

import core.GameSearchService;
import interfaces.GameDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSearchServiceTest {
    private GameSearchService gameService;
    private GameDataProvider dataProvider;

    @BeforeEach
    void setUp() {
        dataProvider = new TestGameDataProvider();
        gameService = new GameSearchService(dataProvider);
    }

    @Test
    void testSearchGameWithValidNameReturnsResults() {
        String result = gameService.searchGame("counter");
        assertNotNull(result);
        assertTrue(result.contains("Counter-Strike"));
        assertTrue(result.contains("AppID"));
    }

    @Test
    void testSearchGameWithShortNameReturnsErrorMessage() {
        String result = gameService.searchGame("a");
        assertEquals("Введите минимум 2 символа для поиска.", result);
    }

    @Test
    void testGetGameInfoWithExistingAppIdReturnsGameInfo() {
        String result = gameService.getGameInfo(730);
        assertNotNull(result);
        assertTrue(result.contains("Counter-Strike"));
        assertTrue(result.contains("730"));
        assertTrue(result.contains("$"));
    }

    @Test
    void testGetGameInfoWithNonExistingAppIdReturnsErrorMessage() {
        String result = gameService.getGameInfo(999999);
        assertTrue(result.contains("не найдена") || result.contains("Ошибка"));
    }

    @Test
    void testGetGameInfoWithFreeGameShowsFree() {
        String result = gameService.getGameInfo(0);
        assertTrue(result.contains("Бесплатно"));
    }

    @Test
    void testGetGameInfoWithDiscountedGameShowsDiscount() {
        String result = gameService.getGameInfo(570);
        assertTrue(result.contains("скидка"));
        assertTrue(result.contains("%"));
    }

    @Test
    void testWelcomeMessageContainsWelcomeText() {
        String welcome = gameService.getWelcomeMessage();
        assertNotNull(welcome);
        assertTrue(welcome.contains("Steam Price Bot") || welcome.contains("Добро пожаловать"));
    }

    @Test
    void testHelpMessageContainsCommands() {
        String help = gameService.getHelpMessage();
        assertNotNull(help);
        assertTrue(help.contains("/search") && help.contains("/info"));
    }

    private static class TestGameDataProvider implements GameDataProvider {
        @Override
        public String getGameInfo(int appId) throws Exception {
            switch (appId) {
                case 730:
                    return "Игра: Counter-Strike: Global Offensive\n\nЦена: $14.99\n\nhttps://store.steampowered.com/app/730";
                case 570:
                    return "Игра: Dota 2\n\nЦена: $9.99 (скидка 50%)\n\nhttps://store.steampowered.com/app/570";
                case 0:
                    return "Игра: Test Free Game\n\nБесплатно\n\nhttps://store.steampowered.com/app/0";
                default:
                    throw new Exception("Game not found");
            }
        }

        @Override
        public String searchGame(String gameName) throws Exception {
            if (gameName.length() < 2) {
                throw new Exception("Search term too short");
            }

            if (gameName.toLowerCase().contains("counter")) {
                return "Найдены игры:\nCounter-Strike: Global Offensive (AppID: 730)\nCounter-Strike (AppID: 10)\n\nИспользуйте команду: info <AppID> для подробной информации";
            }

            return "Игра '" + gameName + "' не найдена.\nПопробуйте другое название или используйте команду: info <AppID>";
        }
    }
}