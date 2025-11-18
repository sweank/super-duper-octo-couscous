package tests.integration;

import core.GameSearchService;
import core.CommandProcessor;
import implementations.SteamApiClient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotIntegrationTest {

    @Test
    void testGameSearchServiceIntegration() {
        GameSearchService gameService = new GameSearchService(new SteamApiClient());

        assertNotNull(gameService);
        assertNotNull(gameService.getWelcomeMessage());
        assertNotNull(gameService.getHelpMessage());
    }

    @Test
    void testCommandProcessorIntegration() {
        GameSearchService gameService = new GameSearchService(new SteamApiClient());
        CommandProcessor processor = new CommandProcessor();

        assertNotNull(processor);
    }
}