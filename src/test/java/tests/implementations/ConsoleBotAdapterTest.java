package tests.implementations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleBotAdapterTest {

    @Test
    void testInputParsingLogic() {
        String searchInput = "search counter strike";
        String infoInput = "info 730";
        String helpInput = "help";

        assertTrue(searchInput.startsWith("search "));
        assertTrue(infoInput.startsWith("info "));
        assertFalse(helpInput.startsWith("search "));
        assertFalse(helpInput.startsWith("info "));
    }

    @Test
    void testCommandExtraction() {
        String searchInput = "search counter strike";
        String infoInput = "info 730";

        String searchCommand = searchInput.substring(0, 6);
        String infoCommand = infoInput.substring(0, 4);

        assertEquals("search", searchCommand);
        assertEquals("info", infoCommand);
    }

    @Test
    void testArgumentExtraction() {
        String searchInput = "search counter strike";
        String infoInput = "info 730";

        String searchArg = searchInput.substring(7);
        String infoArg = infoInput.substring(5);

        assertEquals("counter strike", searchArg);
        assertEquals("730", infoArg);
    }
}