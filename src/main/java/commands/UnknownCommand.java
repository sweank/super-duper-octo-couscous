package commands;

public class UnknownCommand implements Command {
    private final boolean isConsoleMode;

    public UnknownCommand(boolean isConsoleMode) {
        this.isConsoleMode = isConsoleMode;
    }

    @Override
    public String getName() {
        return "unknown";
    }

    @Override
    public String getDescription() {
        return "Неизвестная команда";
    }

    @Override
    public String execute(String argument) {
        if (isConsoleMode) {
            return "Неизвестная команда. Используйте 'help' для списка команд";
        } else {
            return "Неизвестная команда. Используйте /help для списка команд";
        }
    }
}