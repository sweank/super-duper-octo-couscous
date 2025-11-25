package factory;

import commands.*;
import core.GameSearchService;
import core.CommandProcessor;

public class CommandFactory {
    public static CommandProcessor createCommandProcessor(GameSearchService gameService, boolean isConsoleMode) {
        CommandProcessor processor = new CommandProcessor();

        processor.registerCommand("start", new StartCommand(gameService));
        processor.registerCommand("search", new SearchCommand(gameService));
        processor.registerCommand("info", new InfoCommand(gameService));
        processor.registerCommand("help", new HelpCommand(gameService, isConsoleMode));

        return processor;
    }
}