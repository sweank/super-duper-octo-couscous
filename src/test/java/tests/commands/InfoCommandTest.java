package tests.commands;

import commands.InfoCommand;
import core.GameSearchService;
import implementations.SteamApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InfoCommandTest {
    private InfoCommand infoCommand;

    @BeforeEach
    void setUp() {
        GameSearchService gameService = new GameSearchService(new SteamApiClient());
        infoCommand = new InfoCommand(gameService);
    }

    @Test
    void testInfoCommandName() {
        assertEquals("info", infoCommand.getName());
    }

    @Test
    void testInfoCommandDescription() {
        assertEquals("информация об игре по AppID", infoCommand.getDescription());
    }

    @Test
    void testInfoCommandWithEmptyArgument() {
        String result = infoCommand.execute("");
        assertEquals("Укажите AppID после команды /info", result);
    }

    @Test
    void testInfoCommandWithNullArgument() {
        String result = infoCommand.execute(null);
        assertEquals("Укажите AppID после команды /info", result);
    }

    @Test
    void testInfoCommandWithInvalidNumber() {
        String result = infoCommand.execute("abc");
        assertEquals("AppID должен быть числом!", result);
    }
}