package core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TelegramInputParserTest {

    private final TelegramInputParser parser = new TelegramInputParser();

    @Test
    void parseSearchCommand() {
        var result = parser.parse("/search CS:GO");
        assertEquals("search", result.getCommand());
        assertEquals("CS:GO", result.getArgument());
    }

    @Test
    void parseInfoCommand() {
        var result = parser.parse("/info 730");
        assertEquals("info", result.getCommand());
        assertEquals("730", result.getArgument());
    }

    @Test
    void parseImplicitSearch() {
        var result = parser.parse("Dota 2");
        assertEquals("search", result.getCommand());
        assertEquals("Dota 2", result.getArgument());
    }
}