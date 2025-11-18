package tests.commands;

import commands.SearchCommand;
import core.GameSearchService;
import implementations.SteamApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchCommandTest {
    private SearchCommand searchCommand;

    @BeforeEach
    void setUp() {
        GameSearchService gameService = new GameSearchService(new SteamApiClient());
        searchCommand = new SearchCommand(gameService);
    }

    @Test
    void testSearchCommandName() {
        assertEquals("search", searchCommand.getName());
    }

    @Test
    void testSearchCommandDescription() {
        assertEquals("поиск игры по названию", searchCommand.getDescription());
    }

    @Test
    void testSearchCommandWithEmptyArgument() {
        String result = searchCommand.execute("");
        assertEquals("Укажите название игры после команды /search", result);
    }

    @Test
    void testSearchCommandWithNullArgument() {
        String result = searchCommand.execute(null);
        assertEquals("Укажите название игры после команды /search", result);
    }
}