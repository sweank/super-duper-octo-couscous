package tests.commands;

import commands.Command;
import commands.HelpCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

class HelpCommandTest {
    private HelpCommand helpCommand;
    private Map<String, Command> commands;

    @BeforeEach
    void setUp() {
        commands = new HashMap<>();
        commands.put("search", new TestCommand("search", "поиск игры"));
        commands.put("info", new TestCommand("info", "информация об игре"));

        helpCommand = new HelpCommand(commands, true); // console mode
    }

    @Test
    void testHelpCommandName() {
        assertEquals("help", helpCommand.getName());
    }

    @Test
    void testHelpCommandDescription() {
        assertEquals("справка по командам", helpCommand.getDescription());
    }

    @Test
    void testHelpCommandExecution() {
        String result = helpCommand.execute("");
        assertNotNull(result);
        assertTrue(result.contains("search"));
        assertTrue(result.contains("info"));
        assertTrue(result.contains("Справка по командам"));
    }

    static class TestCommand implements Command {
        private final String name;
        private final String description;

        TestCommand(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String execute(String argument) {
            return "test";
        }
    }
}