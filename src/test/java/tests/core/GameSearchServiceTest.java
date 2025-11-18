package tests.core;

import core.GameSearchService;
import implementations.SteamApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameSearchServiceTest {
    private GameSearchService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameSearchService(new SteamApiClient());
    }

    @Test
    void testSearchGameWithShortName() {
        String result = gameService.searchGame("a");
        assertEquals("Введите минимум 2 символа для поиска.", result);
    }

    @Test
    void testSearchGameWithEmptyName() {
        String result = gameService.searchGame("");
        assertEquals("Введите минимум 2 символа для поиска.", result);
    }

    @Test
    void testGetWelcomeMessage() {
        String welcome = gameService.getWelcomeMessage();
        assertNotNull(welcome);
        assertTrue(welcome.contains("Steam Price Bot"));
    }

    @Test
    void testGetHelpMessage() {
        String help = gameService.getHelpMessage();
        assertNotNull(help);
        assertTrue(help.contains("Справка по командам"));
    }
}