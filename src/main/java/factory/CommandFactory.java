package factory;

import commands.*;
import core.GameSearchService;
import core.CommandProcessor;
import implementations.SteamApiClient;
import implementations.TelegramGameService;

public class CommandFactory {
    public static CommandProcessor createCommandProcessor(GameSearchService gameService,
                                                          boolean isConsoleMode) {
        CommandProcessor processor = new CommandProcessor();

        SteamApiClient steamClient = new SteamApiClient();
        TelegramGameService telegramGameService = new TelegramGameService(steamClient);

        processor.registerCommand("start", new StartCommand(gameService));
        processor.registerCommand("search", new SearchCommand(gameService));
        processor.registerCommand("info", new InfoCommand(gameService, telegramGameService, isConsoleMode));
        processor.registerCommand("help", new HelpCommand(gameService, isConsoleMode));

        return processor;
    }
}