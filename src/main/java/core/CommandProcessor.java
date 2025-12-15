package core;

import commands.*;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private final Map<String, Command> commands = new HashMap<>();
    private final Command unknownCommand;

    public CommandProcessor(GameSearchService gameService, boolean isConsoleMode) {
        this.unknownCommand = new UnknownCommand(isConsoleMode);
    }

    public CommandProcessor() {
        this.unknownCommand = new UnknownCommand(false);
    }

    public String processCommand(long userId, String commandName, String argument) {
        Command cmd = commands.get(commandName.toLowerCase());

        if (cmd != null) {
            return cmd.execute(userId, argument);
        }
        return unknownCommand.execute(userId, argument);
    }

    public void registerCommand(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }
}