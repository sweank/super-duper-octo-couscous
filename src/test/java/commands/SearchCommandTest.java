package commands;

import core.GameSearchService;
import org.junit.jupiter.api.Test;
import stubs.StubGameSearchService;
import static org.junit.jupiter.api.Assertions.*;

class SearchCommandTest {

    @Test
    void testExecuteValidSearch() {
        GameSearchService stubService = new StubGameSearchService();
        SearchCommand command = new SearchCommand(stubService);

        String result = command.execute("Half-Life");

        assertEquals("STUB_SEARCH: Half-Life", result);
    }

    @Test
    void testExecuteEmptyArgument() {
        SearchCommand command = new SearchCommand(new StubGameSearchService());

        String result = command.execute("");
        assertTrue(result.contains("Укажите название игры"));

        result = command.execute(null);
        assertTrue(result.contains("Укажите название игры"));
    }
}