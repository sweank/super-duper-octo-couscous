package org.example;

import config.BotConfig;
import config.ConfigLoader;
import core.GameSearchService;
import core.CommandProcessor;
import database.DatabaseHandler;
import implementations.SteamApiClient;
import implementations.ConsoleBotAdapter;
import implementations.TelegramBotAdapter;
import implementations.ConsoleMessenger;
import interfaces.GameDataProvider;
import factory.CommandFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

public class Main {
    private static BotConfig currentConfig;

    public static void main(String[] args) {
        ConfigLoader.load();

        currentConfig = parseArguments(args);

        DatabaseHandler dbHandler = new DatabaseHandler();
        GameDataProvider dataProvider = new SteamApiClient();
        GameSearchService gameService = new GameSearchService(dataProvider, dbHandler);

        boolean isConsoleMode = (currentConfig == BotConfig.CONSOLE);
        CommandProcessor processor = CommandFactory.createCommandProcessor(gameService, dbHandler, isConsoleMode);

        switch (currentConfig) {
            case CONSOLE:
                startConsoleBot(processor);
                break;
            case TELEGRAM:
                startTelegramBot(processor);
                break;
            case BOTH:
                startBothBots(gameService, dbHandler);
                break;
        }
    }

    private static BotConfig parseArguments(String[] args) {
        if (args.length == 0) return showInteractiveMenu();
        switch (args[0].toLowerCase()) {
            case "console": return BotConfig.CONSOLE;
            case "telegram": return BotConfig.TELEGRAM;
            case "both": return BotConfig.BOTH;
            default: return showInteractiveMenu();
        }
    }

    private static BotConfig showInteractiveMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Steam Price Bot - Выбор режима работы");
        System.out.println("===========================================");
        System.out.println("1. Консольный режим");
        System.out.println("2. Telegram режим");
        System.out.println("3. Оба режима");
        System.out.println("===========================================");
        System.out.print("Выберите режим (1-3): ");

        try {
            int choice = scanner.nextInt();
            if (choice == 1) return BotConfig.CONSOLE;
            if (choice == 2) return BotConfig.TELEGRAM;
            return BotConfig.BOTH;
        } catch (Exception e) {
            System.out.println("Ошибка ввода. Запускаю консольный режим.");
            return BotConfig.CONSOLE;
        }
    }

    private static void startConsoleBot(CommandProcessor processor) {
        ConsoleMessenger messenger = new ConsoleMessenger();
        ConsoleBotAdapter bot = new ConsoleBotAdapter(processor, messenger);
        System.out.println("Запуск Steam Price Bot в консольном режиме...");
        bot.start();
    }

    private static void startTelegramBot(CommandProcessor processor) {
        String token = ConfigLoader.get("TELEGRAM_BOT_TOKEN");
        String username = getBotUsername(token);

        if (token == null || token.isEmpty()) {
            System.err.println("ОШИБКА: Токен Telegram бота не найден в .env файле!");
            return;
        }

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBotAdapter bot = new TelegramBotAdapter(processor, username, token);
            botsApi.registerBot(bot);
            System.out.println("Telegram бот успешно запущен (@" + username + ")");
        } catch (Exception e) {
            System.err.println("Ошибка запуска Telegram бота: " + e.getMessage());
        }
    }

    private static void startBothBots(GameSearchService gameService, DatabaseHandler dbHandler) {
        System.out.println("Запуск Steam Price Bot в обоих режимах...");

        CommandProcessor consoleProc = CommandFactory.createCommandProcessor(gameService, dbHandler, true);
        CommandProcessor telegramProc = CommandFactory.createCommandProcessor(gameService, dbHandler, false);

        Thread tgThread = new Thread(() -> startTelegramBot(telegramProc));
        tgThread.setDaemon(true);
        tgThread.start();

        try { Thread.sleep(1000); } catch (Exception e) {}

        System.out.println("\nЗапуск консольного интерфейса...");
        startConsoleBot(consoleProc);
    }

    private static String getBotUsername(String token) {
        return (token != null && token.contains(":")) ? token.split(":")[0] + "_bot" : "steam_bot";
    }
}