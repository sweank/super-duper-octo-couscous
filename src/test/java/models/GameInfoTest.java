package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameInfoTest {

    @Test
    void testFormatForConsole() {
        GameInfo game = new GameInfo(
                "Test Game", 123, 10.0, 20.0, "USD", 50, false,
                "url", "desc", "2023", "Dev", "Pub", new String[]{"Action"}
        );

        String result = game.formatForConsole();

        assertTrue(result.contains("Игра: Test Game"));
        assertTrue(result.contains("AppID: 123"));
        assertTrue(result.contains("50%"));
    }

    @Test
    void testFormatForTelegramFree() {
        GameInfo game = new GameInfo(
                "Free Game", 456, 0.0, 0.0, "USD", 0, true,
                "url", "desc", "2023", "Dev", "Pub", new String[]{"Indie"}
        );

        String result = game.formatForTelegram();

        assertTrue(result.contains("*Free Game*"));
        assertTrue(result.contains("*Бесплатно*"));
    }
}