package commands;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnknownCommandTest {

    @Test
    void testExecuteConsole() {
        UnknownCommand command = new UnknownCommand(true);
        String result = command.execute(1L, "blabla");

        assertTrue(result.contains("'help'"));
        assertTrue(result.contains("Неизвестная команда"));
    }

    @Test
    void testExecuteTelegram() {
        UnknownCommand command = new UnknownCommand(false);
        String result = command.execute(1L, "blabla");

        assertTrue(result.contains("/help"));
    }
}