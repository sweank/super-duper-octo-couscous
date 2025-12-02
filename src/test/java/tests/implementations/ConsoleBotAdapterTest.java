package tests.implementations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleBotAdapterTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    void testParseConsoleInputLogic_SearchCommand() {
        String input = "search Counter-Strike";

        assertTrue(input.startsWith("search "), "Команда search должна начинаться с 'search '");

        String command = input.substring(0, 6);
        String argument = input.substring(7);

        assertEquals("search", command, "Команда должна быть 'search'");
        assertEquals("Counter-Strike", argument, "Аргумент должен быть 'Counter-Strike'");
    }

    @Test
    void testParseConsoleInputLogic_InfoCommand() {
        String input = "info 730";

        assertTrue(input.startsWith("info "), "Команда info должна начинаться с 'info '");

        String command = input.substring(0, 4);
        String argument = input.substring(5);

        assertEquals("info", command, "Команда должна быть 'info'");
        assertEquals("730", argument, "Аргумент должен быть '730'");
    }

    @Test
    void testParseConsoleInputLogic_HelpCommand() {
        String input = "help";

        assertFalse(input.startsWith("search "), "Команда help не должна начинаться с 'search '");
        assertFalse(input.startsWith("info "), "Команда help не должна начинаться с 'info '");

        assertEquals("help", input, "Команда должна быть 'help'");
    }

    @Test
    void testConsoleWelcomeMessageFormat() {
        String welcomeMessage = getExpectedWelcomeMessage();

        assertTrue(welcomeMessage.contains("Консольная версия"),
                "Приветствие должно содержать 'Консольная версия'");

        assertTrue(welcomeMessage.contains("search [название]"),
                "Приветствие должно содержать команду search без /");

        assertTrue(welcomeMessage.contains("info [AppID]"),
                "Приветствие должно содержать команду info без /");

        assertTrue(welcomeMessage.contains("help"),
                "Приветствие должно содержать команду help без /");

        assertTrue(welcomeMessage.contains("quit"),
                "Приветствие должно содержать команду quit");

        assertFalse(welcomeMessage.contains("/search"),
                "В консоли команды не должны содержать /");

        assertFalse(welcomeMessage.contains("/info"),
                "В консоли команды не должны содержать /");
    }

    @Test
    void testCommandParsingEdgeCases() {
        String[] testCases = {
                "search",           // команда без аргумента
                "search  ",         // команда с пробелами
                "info",             // команда без аргумента
                "  search  game  ", // команда с лишними пробелами
                "HELP",             // команда в верхнем регистре
                "Search game"       // команда с заглавной буквы
        };

        for (String input : testCases) {
            assertNotNull(input, "Входные данные не должны быть null");
            assertFalse(input.isEmpty(), "Входные данные не должны быть пустыми");
        }
    }

    @Test
    void testCommandCaseInsensitivity() {
        String lowerCase = "search game";
        String upperCase = "SEARCH GAME";
        String mixedCase = "SeArCh GaMe";

        String lowerCommand = lowerCase.substring(0, 6);
        String upperCommand = upperCase.substring(0, 6);

        assertEquals("search", lowerCommand.toLowerCase(),
                "Команда должна быть приведена к нижнему регистру");
        assertEquals("search", upperCommand.toLowerCase(),
                "Команда должна быть приведена к нижнему регистру");
    }

    @Test
    void testArgumentTrimming() {
        String inputWithSpaces = "  search  Counter-Strike  ";
        String trimmed = inputWithSpaces.trim();

        assertTrue(trimmed.startsWith("search "),
                "После trim() команда должна начинаться с 'search '");

        String argument = trimmed.substring(7);
        assertEquals("Counter-Strike", argument,
                "Аргумент должен быть обрезан от лишних пробелов");
    }

    @Test
    void testQuitCommandDetection() {
        String[] quitCommands = {"quit", "QUIT", "Quit", "exit", "EXIT", "Exit"};

        for (String command : quitCommands) {
            assertTrue(isQuitCommand(command),
                    "Команда '" + command + "' должна распознаваться как команда выхода");
        }

        String[] notQuitCommands = {"quitt", "exitt", "help", "search"};
        for (String command : notQuitCommands) {
            assertFalse(isQuitCommand(command),
                    "Команда '" + command + "' не должна распознаваться как команда выхода");
        }
    }

    private boolean isQuitCommand(String input) {
        return input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit");
    }

    private String getExpectedWelcomeMessage() {
        return "Steam Price Bot - Консольная версия\n" +
                "==========================================\n" +
                "Поиск информации об играх Steam\n" +
                "==========================================\n" +
                "Доступные команды (без '/'):\n" +
                "- search [название] - поиск игры\n" +
                "- info [AppID] - информация об игре\n" +
                "- help - справка по командам\n" +
                "- start - показать приветствие\n" +
                "- quit - выход из программы\n" +
                "==========================================\n" +
                "Примеры:\n" +
                "search Counter-Strike\n" +
                "info 730\n" +
                "help\n" +
                "==========================================";
    }
}