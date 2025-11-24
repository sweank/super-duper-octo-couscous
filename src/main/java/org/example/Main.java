package org.example;

import config.BotConfig;
import config.EnvironmentConfig;
import core.GameSearchService;
import core.CommandProcessor;
import implementations.SteamApiClient;
import implementations.ConsoleBotAdapter;
import implementations.TelegramBotAdapter;
import implementations.ConsoleMessenger;
import interfaces.GameDataProvider;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BotConfig selectedMode = selectMode();
        startBot(selectedMode);
    }

    private static BotConfig selectMode() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Steam Price Bot Launcher ===");
        System.out.println("Выберите режим работы:");
        System.out.println("1 - Консольный режим");

        if (isTelegramTokenAvailable()) {
            System.out.println("2 - Telegram бот");
            System.out.println("3 - Оба режима одновременно");
        } else {
            System.out.println("2 - Telegram бот (недоступен - токен не настроен)");
            System.out.println("3 - Оба режима одновременно (недоступен - токен не настроен)");
        }

        System.out.print("Ваш выбор: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            boolean telegramAvailable = isTelegramTokenAvailable();

            switch (choice) {
                case 1:
                    return BotConfig.CONSOLE;
                case 2:
                    return telegramAvailable ? BotConfig.TELEGRAM : BotConfig.CONSOLE;
                case 3:
                    return telegramAvailable ? BotConfig.BOTH : BotConfig.CONSOLE;
                default:
                    System.out.println("Неверный выбор. Запускаю консольный режим.");
                    return BotConfig.CONSOLE;
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода. Запускаю консольный режим.");
            return BotConfig.CONSOLE;
        }
    }

    private static void startBot(BotConfig mode) {
        GameDataProvider dataProvider = new SteamApiClient();
        GameSearchService gameService = new GameSearchService(dataProvider);

        switch (mode) {
            case CONSOLE:
                startConsoleMode(gameService);
                break;
            case TELEGRAM:
                startTelegramMode(gameService);
                break;
            case BOTH:
                startBothModes(gameService);
                break;
        }
    }

    private static void startConsoleMode(GameSearchService gameService) {
        System.out.println("Запуск консольного режима...");
        CommandProcessor processor = new CommandProcessor(gameService, true);
        ConsoleMessenger consoleMessenger = new ConsoleMessenger();
        ConsoleBotAdapter consoleBot = new ConsoleBotAdapter(processor, consoleMessenger);
        consoleBot.start();
    }

    private static void startTelegramMode(GameSearchService gameService) {
        System.out.println("Запуск Telegram режима...");
        String token = getTelegramToken();
        if (token == null) {
            System.out.println("Не удалось запустить Telegram бот. Переключаюсь на консольный режим.");
            startConsoleMode(gameService);
            return;
        }

        CommandProcessor processor = new CommandProcessor(gameService, false);
        TelegramBotAdapter telegramBot = new TelegramBotAdapter(processor, "steam_price_bot", token);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
            System.out.println("Telegram бот успешно запущен!");
        } catch (Exception e) {
            System.err.println("Ошибка при запуске Telegram бота: " + e.getMessage());
            System.out.println("Переключаюсь на консольный режим.");
            startConsoleMode(gameService);
        }
    }

    private static void startBothModes(GameSearchService gameService) {
        System.out.println("Запуск обоих режимов...");

        new Thread(() -> {
            System.out.println("Запуск консольного режима в фоне...");
            startConsoleMode(gameService);
        }).start();

        startTelegramMode(gameService);
    }

    private static String getTelegramToken() {
        try {
            return EnvironmentConfig.getTelegramBotToken();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static boolean isTelegramTokenAvailable() {
        return EnvironmentConfig.isTelegramTokenAvailable();
    }
}
