package commands;

import java.util.Map;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;
    private final boolean isConsoleMode;

    public HelpCommand(Map<String, Command> commands, boolean isConsoleMode) {
        this.commands = commands;
        this.isConsoleMode = isConsoleMode;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "справка по командам";
    }

    @Override
    public String execute(String argument) {
        if (isConsoleMode) {
            return getConsoleHelp();
        } else {
            return getTelegramHelp();
        }
    }

    private String getConsoleHelp() {
        StringBuilder helpText = new StringBuilder();
        helpText.append("Справка по командам (консольная версия):\n\n");

        commands.values().forEach(command -> {
            helpText.append("- ").append(command.getName())
                    .append(" - ").append(command.getDescription())
                    .append("\n");
        });

        helpText.append("\nПримеры использования:\n");
        helpText.append("search Counter-Strike\n");
        helpText.append("info 730\n");
        helpText.append("help\n");
        helpText.append("quit\n");

        return helpText.toString();
    }

    private String getTelegramHelp() {
        StringBuilder helpText = new StringBuilder();
        helpText.append("Steam Price Bot\n\n");
        helpText.append("Доступные команды:\n\n");

        commands.values().forEach(command -> {
            helpText.append("- /").append(command.getName())
                    .append(" - ").append(command.getDescription())
                    .append("\n");
        });

        helpText.append("\nПримеры использования:\n");
        helpText.append("/search Counter-Strike\n");
        helpText.append("/info 730\n");
        helpText.append("/help\n");

        return helpText.toString();
    }
}