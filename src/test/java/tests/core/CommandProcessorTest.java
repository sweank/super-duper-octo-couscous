package tests.core;

import commands.Command;
import core.CommandProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandProcessorTest {
    private CommandProcessor commandProcessor;

    @BeforeEach
    void setUp() {
        commandProcessor = new CommandProcessor();
    }

    @Test
    void testRegisterCustomCommand() {
        Command customCommand = new Command() {
            @Override
            public String getName() {
                return "custom";
            }

            @Override
            public String getDescription() {
                return "custom command";
            }

            @Override
            public String execute(String argument) {
                return "Custom response: " + argument;
            }
        };

        commandProcessor.registerCommand(customCommand);

        String result = commandProcessor.processCommand("custom", "test");
        assertEquals("Custom response: test", result);
    }

    @Test
    void testRegisterCustomCommandWithName() {
        Command customCommand = new Command() {
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
                return "Test response";
            }
        };

        commandProcessor.registerCommand("mycommand", customCommand);

        String result = commandProcessor.processCommand("mycommand", "");
        assertEquals("Test response", result);
    }
}