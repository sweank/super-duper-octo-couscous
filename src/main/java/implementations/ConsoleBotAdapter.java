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
        messenger.sendMessage(processor.processCommand("start", ""));
        messenger.sendMessage("💡 Команды: search [название], info [AppID], help, quit");

        while (true) {
            String input = messenger.receiveMessage().trim();

            if ("quit".equals(input)) {
                messenger.sendMessage("👋 До свидания!");
                break;
            }

            String[] parts = parseConsoleInput(input);
            String response = processor.processCommand(parts[0], parts[1]);
            messenger.sendMessage(response);
        }

    }

    private String[] parseConsoleInput(String input) {
        if (input.startsWith("search ")) {
            return new String[]{"search", input.substring(7)};
        } else if (input.startsWith("info ")) {
            return new String[]{"info", input.substring(5)};
        } else {
            return new String[]{input, ""};
        }
    }
}