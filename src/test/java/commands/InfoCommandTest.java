package commands;

import org.junit.jupiter.api.Test;
import stubs.StubGameSearchService;
import stubs.StubTelegramGameService;
import static org.junit.jupiter.api.Assertions.*;

class InfoCommandTest {

    @Test
    void testConsoleModeExecution() {
        InfoCommand command = new InfoCommand(new StubGameSearchService());

        String result = command.execute("730");

        assertEquals("STUB_INFO: 730", result);
    }

    @Test
    void testTelegramModeExecution() {
        InfoCommand command = new InfoCommand(
                new StubGameSearchService(),
                new StubTelegramGameService(),
                false
        );

        String result = command.execute("730");

        assertEquals("STUB_TG_IMAGE: 730", result);
    }

    @Test
    void testInvalidInput() {
        InfoCommand command = new InfoCommand(new StubGameSearchService());

        String result = command.execute("abc");
        assertEquals("AppID должен быть числом!", result);

        result = command.execute("");
        assertTrue(result.contains("Укажите AppID"));
    }
}