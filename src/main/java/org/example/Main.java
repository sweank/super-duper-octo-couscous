package org.example;

import core.GameSearchService;
import core.CommandProcessor;
import implementations.SteamApiClient;
import implementations.ConsoleBotAdapter;
import implementations.TelegramBotAdapter;
import implementations.ConsoleMessenger;
import interfaces.IGameDataProvider;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        IGameDataProvider dataProvider = new SteamApiClient();
        GameSearchService gameService = new GameSearchService(dataProvider);
        CommandProcessor processor = new CommandProcessor(gameService);

        ConsoleMessenger consoleMessenger = new ConsoleMessenger();
        ConsoleBotAdapter consoleBot = new ConsoleBotAdapter(processor, consoleMessenger);

        TelegramBotAdapter telegramBot = new TelegramBotAdapter(processor, "deku1hr_bot", "8533139513:AAFpmH0gO1IwIodx3t-H4P-hMvDx3JyY0Uc");

        new Thread(consoleBot::start).start();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}