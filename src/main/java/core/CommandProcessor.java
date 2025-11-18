package core;

import commands.Command;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public String processCommand(String commandName, String argument) {
        Command command = commands.get(commandName.toLowerCase());
        if (command != null) {
            return command.execute(argument);
        }
        return "Неизвестная команда. Используйте /help для списка команд.";
    }

    public Map<String, Command> getCommands() {
        return new HashMap<>(commands);
    }
}