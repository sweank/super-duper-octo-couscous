package implementations;

import interfaces.IMessenger;

import java.util.Scanner;


public class ConsoleMessenger implements IMessenger {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void sendMessage(String text) {
        System.out.println(text);
    }

    @Override
    public String receiveMessage() {
        return scanner.nextLine();
    }
}