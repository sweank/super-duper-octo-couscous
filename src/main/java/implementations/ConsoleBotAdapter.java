package implementations;

import core.CommandProcessor;
import interfaces.Messenger;

public class ConsoleBotAdapter {
    private final CommandProcessor processor;
    private final Messenger messenger;
    private final long CONSOLE_USER_ID = 0L;

    public ConsoleBotAdapter(CommandProcessor processor, Messenger messenger) {
        this.processor = processor;
        this.messenger = messenger;
    }

    public void start() {
        showWelcomeMessage();

        while (true) {
            String input = messenger.receiveMessage().trim();

            if ("quit".equalsIgnoreCase(input) || "exit".equalsIgnoreCase(input)) {
                messenger.sendMessage("До свидания! Спасибо за использование Steam Price Bot!");
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = parseConsoleInput(input);
            String response = processor.processCommand(CONSOLE_USER_ID, parts[0], parts[1]);
            messenger.sendMessage(response);
        }
    }

    private void showWelcomeMessage() {
        StringBuilder welcomeMessage = new StringBuilder();
        welcomeMessage.append("Steam Price Bot - Консольная версия\n");
        welcomeMessage.append("==========================================\n");
        welcomeMessage.append("Поиск информации об играх Steam\n");
        welcomeMessage.append("==========================================\n");
        welcomeMessage.append("Доступные команды (без '/'):\n");
        welcomeMessage.append("- search [название] - поиск игры\n");
        welcomeMessage.append("- info [AppID] - информация об игре\n");
        welcomeMessage.append("- history - история ваших запросов\n");
        welcomeMessage.append("- help - справка по командам\n");
        welcomeMessage.append("- start - показать приветствие\n");
        welcomeMessage.append("- quit - выход из программы\n");
        welcomeMessage.append("==========================================\n");
        welcomeMessage.append("Примеры:\n");
        welcomeMessage.append("search Counter-Strike\n");
        welcomeMessage.append("info 730\n");
        welcomeMessage.append("history\n");
        welcomeMessage.append("help\n");
        welcomeMessage.append("==========================================");

        messenger.sendMessage(welcomeMessage.toString());
    }

    private String[] parseConsoleInput(String input) {
        if (input.startsWith("search ")) {
            return new String[]{"search", input.substring(7).trim()};
        } else if (input.startsWith("info ")) {
            return new String[]{"info", input.substring(5).trim()};
        } else if (input.startsWith("history ")) {
            return new String[]{"history", input.substring(8).trim()};
        } else {
            String[] split = input.split(" ", 2);
            String command = split[0].toLowerCase();
            String arg = split.length > 1 ? split[1].trim() : "";
            return new String[]{command, arg};
        }
    }
}