package implementations;

import core.CommandProcessor;
import interfaces.IMessenger;

public class ConsoleBotAdapter {
    private final CommandProcessor processor;
    private final IMessenger messenger;

    public ConsoleBotAdapter(CommandProcessor processor, IMessenger messenger) {
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
            String response = processor.processCommand(parts[0], parts[1]);
            messenger.sendMessage(response);
        }
    }

    private void showWelcomeMessage() {
        String welcomeMessage = """
            Steam Price Bot - Консольная версия
            ==========================================
            Поиск информации об играх Steam
            ==========================================
            Доступные команды (без '/'):
            - search [название] - поиск игры
            - info [AppID] - информация об игре
            - help - справка по командам
            - start - показать приветствие
            - quit - выход из программы
            ==========================================
            Примеры:
            search Counter-Strike
            info 730
            help
            ==========================================""";

        messenger.sendMessage(welcomeMessage);
    }

    private String[] parseConsoleInput(String input) {
        if (input.startsWith("search ")) {
            return new String[]{"search", input.substring(7).trim()};
        } else if (input.startsWith("info ")) {
            return new String[]{"info", input.substring(5).trim()};
        } else {
            return new String[]{input.toLowerCase(), ""};
        }
    }
}