package implementations;

import interfaces.Messenger;
import java.util.Scanner;

public class ConsoleMessenger implements Messenger {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void sendMessage(String text) {
        System.out.println(text);
    }

    @Override
    public String receiveMessage() {
        System.out.print("> ");
        return scanner.nextLine();
    }
}