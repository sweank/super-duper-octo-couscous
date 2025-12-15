package commands;

import core.GameSearchService;
import java.util.Map;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;
    private final boolean isConsoleMode;
    private final GameSearchService gameService;

    public HelpCommand(Map<String, Command> commands, boolean isConsoleMode) {
        this.commands = commands;
        this.isConsoleMode = isConsoleMode;
        this.gameService = null;
    }

    public HelpCommand(GameSearchService gameService, boolean isConsoleMode) {
        this.commands = null;
        this.isConsoleMode = isConsoleMode;
        this.gameService = gameService;
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
    public String execute(long userId, String argument) {
        if (gameService != null) {
            return getAdaptedHelp();
        } else if (commands != null) {
            return generateHelpFromCommands();
        } else {
            return "Справка недоступна";
        }
    }

    private String generateHelpFromCommands() {
        if (isConsoleMode) {
            return getConsoleHelp();
        } else {
            return getTelegramHelp();
        }
    }

    private String getAdaptedHelp() {
        if (isConsoleMode) {
            return getConsoleAdaptedHelp();
        } else {
            return getTelegramAdaptedHelp();
        }
    }

    private String getConsoleAdaptedHelp() {
        return "Steam Price Bot - Справка\n\n" +
                "Основные команды:\n" +
                "search [название] - поиск игры по названию\n" +
                "info [AppID] - информация об игре\n" +
                "history - история ваших запросов\n" +
                "help - эта справка\n" +
                "start - начальное меню\n" +
                "quit - выход из программы\n\n" +
                "Примеры использования:\n" +
                "search Counter-Strike\n" +
                "info 730\n" +
                "history\n" +
                "help\n\n" +
                "Подсказка: для консоли не нужен символ / перед командами";
    }

    private String getTelegramAdaptedHelp() {
        return "Steam Price Bot - Справка\n\n" +
                "Основные команды:\n" +
                "/search [название] - поиск игры по названию\n" +
                "/info [AppID] - информация об игре\n" +
                "/history - история поиска\n" +
                "/help - эта справка\n" +
                "/start - начальное меню\n\n" +
                "Примеры использования:\n" +
                "/search Counter-Strike\n" +
                "/info 730\n" +
                "/history\n" +
                "/help\n\n" +
                "Подсказка: также используйте кнопки внизу экрана";
    }

    private String getConsoleHelp() {
        StringBuilder helpText = new StringBuilder();
        helpText.append("Справка по командам (консольная версия):\n\n");

        if (commands != null) {
            commands.values().forEach(command -> {
                helpText.append("- ").append(command.getName())
                        .append(" - ").append(command.getDescription())
                        .append("\n");
            });
        }

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

        if (commands != null) {
            commands.values().forEach(command -> {
                helpText.append("- /").append(command.getName())
                        .append(" - ").append(command.getDescription())
                        .append("\n");
            });
        }

        helpText.append("\nПримеры использования:\n");
        helpText.append("/search Counter-Strike\n");
        helpText.append("/info 730\n");
        helpText.append("/help\n");

        return helpText.toString();
    }
}