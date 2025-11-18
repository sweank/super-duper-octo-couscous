package tests.commands;

import commands.StartCommand;
import core.GameSearchService;
import implementations.SteamApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StartCommandTest {
    private StartCommand startCommand;

    @BeforeEach
    void setUp() {
        GameSearchService gameService = new GameSearchService(new SteamApiClient());
        startCommand = new StartCommand(gameService);
    }

    @Test
    void testStartCommandName() {
        assertEquals("start", startCommand.getName());
    }

    @Test
    void testStartCommandDescription() {
        assertEquals("начало работы с ботом", startCommand.getDescription());
    }

    @Test
    void testStartCommandExecution() {
        String result = startCommand.execute("");
        assertNotNull(result);
        assertTrue(result.contains("Steam Price Bot"));
    }
}