package core;

import commands.*;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private final Map<String, Command> commands = new HashMap<>();
    private final Command unknownCommand;

    public CommandProcessor(GameSearchService gameService, boolean isConsoleMode) {
        commands.put("search", new SearchCommand(gameService));
        commands.put("info", new InfoCommand(gameService));
        commands.put("help", new HelpCommand(gameService, isConsoleMode));
        commands.put("start", new StartCommand(gameService));

        this.unknownCommand = new UnknownCommand(isConsoleMode);
    }

    public CommandProcessor() {
        this.unknownCommand = new UnknownCommand(false);
    }

    public String processCommand(String command, String argument) {
        Command cmd = commands.get(command.toLowerCase());
        if (cmd != null) {
            return cmd.execute(argument);
        }
        return unknownCommand.execute(argument);
    }

    public void registerCommand(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    public void registerCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }
}