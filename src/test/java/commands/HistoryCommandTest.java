package commands;

import core.GameSearchService;
import org.junit.jupiter.api.Test;
import stubs.StubGameSearchService;
import static org.junit.jupiter.api.Assertions.*;

class HistoryCommandTest {

    @Test
    void testExecute() {
        GameSearchService stubService = new StubGameSearchService();
        HistoryCommand command = new HistoryCommand(stubService);

        String result = command.execute(123L, "dota");

        assertEquals("history", command.getName());
        assertEquals("STUB_HISTORY: dota", result);
    }
}