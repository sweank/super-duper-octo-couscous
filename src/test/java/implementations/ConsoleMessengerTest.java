package implementations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleMessengerTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    @DisplayName("sendMessage должен печатать сообщение в System.out")
    void sendMessage_ShouldPrintMessageToSystemOut() {
        ConsoleMessenger messenger = new ConsoleMessenger();
        String message = "Тестовое сообщение";

        messenger.sendMessage(message);

        assertEquals(message + System.lineSeparator(), outContent.toString());
    }

    @Test
    @DisplayName("receiveMessage должен читать строку из System.in")
    void receiveMessage_ShouldReadStringFromSystemIn() {
        ConsoleMessenger messenger = new ConsoleMessenger();
        String input = "Ввод от пользователя";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        String result = messenger.receiveMessage();

        assertEquals(input, result);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
}