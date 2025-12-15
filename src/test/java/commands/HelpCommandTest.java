package commands;

import org.junit.jupiter.api.Test;
import stubs.StubGameSearchService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest {

    @Test
    void testConsoleHelp() {
        Map<String, Command> commands = new HashMap<>();
        commands.put("start", new StartCommand(new StubGameSearchService()));

        HelpCommand helpCommand = new HelpCommand(commands, true);

        String result = helpCommand.execute(1L, "");

        assertTrue(result.contains("Справка по командам (консольная версия)"));
        assertTrue(result.contains("- start"));
    }

    @Test
    void testTelegramHelp() {
        HelpCommand helpCommand = new HelpCommand(new StubGameSearchService(), false);

        String result = helpCommand.execute(1L, "");

        assertTrue(result.contains("Steam Price Bot - Справка"));
        assertTrue(result.contains("/search"));
        assertTrue(result.contains("/history"));
    }
}