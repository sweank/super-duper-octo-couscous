package factory;

import commands.*;
import core.GameSearchService;
import core.CommandProcessor;

public class CommandFactory {
    public static CommandProcessor createCommandProcessor(GameSearchService gameService, boolean isConsoleMode) {
        CommandProcessor processor = new CommandProcessor();

        processor.registerCommand(new StartCommand(gameService));
        processor.registerCommand(new SearchCommand(gameService));
        processor.registerCommand(new InfoCommand(gameService));

        processor.registerCommand(new HelpCommand(processor.getCommands(), isConsoleMode));

        return processor;
    }
}