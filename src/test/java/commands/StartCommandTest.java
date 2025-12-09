package commands;

import core.GameSearchService;
import org.junit.jupiter.api.Test;
import stubs.StubGameSearchService;
import static org.junit.jupiter.api.Assertions.*;

class StartCommandTest {

    @Test
    void testExecute() {
        GameSearchService stubService = new StubGameSearchService();
        StartCommand command = new StartCommand(stubService);

        String result = command.execute("");

        assertEquals("start", command.getName());
        assertEquals("STUB_WELCOME", result);
    }
}