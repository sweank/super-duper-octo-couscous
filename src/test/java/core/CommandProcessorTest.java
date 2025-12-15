package core;

import commands.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandProcessorTest {

    private CommandProcessor processor;

    private static class StubCommand implements Command {
        @Override
        public String getName() {
            return "test";
        }

        @Override
        public String getDescription() {
            return "test desc";
        }

        @Override
        public String execute(long userId, String argument) {
            return "USER:" + userId + " EXECUTED: " + argument;
        }
    }

    @BeforeEach
    void setUp() {
        processor = new CommandProcessor();

        processor.registerCommand("test", new StubCommand());
    }

    @Test
    void testProcessExistingCommand() {
        String result = processor.processCommand(555L, "TEST", "arg123");

        assertEquals("USER:555 EXECUTED: arg123", result);
    }

    @Test
    void testProcessUnknownCommand() {
        String result = processor.processCommand(1L, "abrakadabra", "");

        assertNotEquals("EXECUTED: ", result);
        assertTrue(result.contains("Неизвестная команда"));
    }
}