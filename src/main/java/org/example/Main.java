package org.example;

import config.BotConfig;
import core.GameSearchService;
import core.CommandProcessor;
import implementations.SteamApiClient;
import implementations.ConsoleBotAdapter;
import implementations.TelegramBotAdapter;
import implementations.ConsoleMessenger;
import interfaces.IGameDataProvider;
import factory.CommandFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.Scanner;

public class Main {
    private static final String TELEGRAM_BOT_TOKEN = "8533139513:AAFpmH0gO1IwIodx3t-H4P-hMvDx3JyY0Uc";
    private static final String TELEGRAM_BOT_USERNAME = "deku1hr_bot";
    private static BotConfig currentConfig;

    public static void main(String[] args) {
        currentConfig = parseArguments(args);

        IGameDataProvider dataProvider = new SteamApiClient();
        GameSearchService gameService = new GameSearchService(dataProvider);

        boolean isConsoleMode = (currentConfig == BotConfig.CONSOLE);
        CommandProcessor processor = CommandFactory.createCommandProcessor(gameService, isConsoleMode);

        switch (currentConfig) {
            case CONSOLE:
                startConsoleBot(processor);
                break;
            case TELEGRAM:
                startTelegramBot(processor);
                break;
            case BOTH:
                startBothBots(gameService);
                break;
        }
    }

    private static BotConfig parseArguments(String[] args) {
        if (args.length == 0) {
            return showInteractiveMenu();
        }

        switch (args[0].toLowerCase()) {
            case "console":
                return BotConfig.CONSOLE;
            case "telegram":
                return BotConfig.TELEGRAM;
            case "both":
                return BotConfig.BOTH;
            default:
                System.out.println("Неизвестный режим: " + args[0]);
                System.out.println("Использование: java -jar steam-bot.jar [console|telegram|both]");
                return showInteractiveMenu();
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
            switch (choice) {
                case 1:
                    return BotConfig.CONSOLE;
                case 2:
                    return BotConfig.TELEGRAM;
                case 3:
                    return BotConfig.BOTH;
                default:
                    System.out.println("Неверный выбор. Запускаю консольный режим.");
                    return BotConfig.CONSOLE;
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода. Запускаю консольный режим.");
            return BotConfig.CONSOLE;
        }
    }

    private static void startConsoleBot(CommandProcessor processor) {
        ConsoleMessenger consoleMessenger = new ConsoleMessenger();
        ConsoleBotAdapter consoleBot = new ConsoleBotAdapter(processor, consoleMessenger);

        System.out.println("Запуск Steam Price Bot в консольном режиме...");
        System.out.println("===========================================");

        consoleBot.start();
    }

    private static void startTelegramBot(CommandProcessor processor) {
        String token = TELEGRAM_BOT_TOKEN;

        if (token == null || token.trim().isEmpty()) {
            System.err.println("ОШИБКА: Токен не настроен!");
            return;
        }

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBotAdapter telegramBot = new TelegramBotAdapter(processor, TELEGRAM_BOT_USERNAME, token);
            botsApi.registerBot(telegramBot);
            System.out.println("Telegram бот успешно запущен!");
            System.out.println("Имя бота: @" + TELEGRAM_BOT_USERNAME);
            System.out.println("Токен: " + maskToken(token));
            System.out.println("Ожидание сообщений...");
            System.out.println("Для остановки нажмите Ctrl+C");
        } catch (Exception e) {
            System.err.println("Ошибка при запуске Telegram бота: " + e.getMessage());
            System.err.println("Проверьте правильность токена и подключение к интернету");
        }
    }

    private static void startBothBots(GameSearchService gameService) {
        System.out.println("Запуск Steam Price Bot в обоих режимах...");
        System.out.println("===========================================");

        CommandProcessor consoleProcessor = CommandFactory.createCommandProcessor(gameService, true);
        CommandProcessor telegramProcessor = CommandFactory.createCommandProcessor(gameService, false);

        Thread telegramThread = new Thread(() -> startTelegramBot(telegramProcessor));
        telegramThread.setDaemon(true);
        telegramThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nЗапуск консольного интерфейса...");
        System.out.println("===========================================");

        startConsoleBot(consoleProcessor);
    }

    private static String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}