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
        public String execute(String argument) {
            return "EXECUTED: " + argument;
        }
    }

    @BeforeEach
    void setUp() {
        processor = new CommandProcessor();
        processor.registerCommand(new StubCommand());
    }

    @Test
    void testProcessExistingCommandCaseInsensitive() {

        String result = processor.processCommand("TEST", "arg123");

        if (!result.equals("EXECUTED: arg123")) {
            System.out.println("!!! ОШИБКА ТЕСТА !!!");
            System.out.println("Ожидали: EXECUTED: arg123");
            System.out.println("Получили: " + result);
            System.out.println("Если вы видите кракозябры выше, значит сработала UnknownCommand.");
        }

        assertEquals("EXECUTED: arg123", result);
    }

    @Test
    void testProcessUnknownCommand() {
        String result = processor.processCommand("abrakadabra", "");

        assertNotEquals("EXECUTED: ", result);
        assertNotNull(result);
    }
}