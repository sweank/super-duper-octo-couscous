package tests.core;

import commands.Command;
import core.CommandProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandProcessorTest {
    private CommandProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new CommandProcessor();
    }

    @Test
    void testRegisterCommand() {
        TestCommand testCommand = new TestCommand();
        processor.registerCommand(testCommand);

        String result = processor.processCommand("test", "arg");
        assertEquals("test executed with: arg", result);
    }

    @Test
    void testProcessRegisteredCommand() {
        processor.registerCommand(new TestCommand());
        String result = processor.processCommand("test", "argument");
        assertEquals("test executed with: argument", result);
    }

    @Test
    void testProcessUnknownCommand() {
        String result = processor.processCommand("unknown", "argument");
        assertEquals("Неизвестная команда. Используйте /help для списка команд.", result);
    }

    @Test
    void testCommandCaseInsensitive() {
        processor.registerCommand(new TestCommand());
        String result = processor.processCommand("TEST", "argument");
        assertEquals("test executed with: argument", result);
    }

    static class TestCommand implements Command {
        @Override
        public String getName() {
            return "test";
        }

        @Override
        public String getDescription() {
            return "test command";
        }

        @Override
        public String execute(String argument) {
            return "test executed with: " + argument;
        }
    }
}