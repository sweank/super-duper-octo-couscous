package org.example;

import config.BotConfig;
import core.GameSearchService;
import core.CommandProcessor;
import implementations.SteamApiClient;
import implementations.ConsoleBotAdapter;
import implementations.TelegramBotAdapter;
import implementations.ConsoleMessenger;
import interfaces.GameDataProvider;
import factory.CommandFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static BotConfig currentConfig;

    public static void main(String[] args) {
        currentConfig = parseArguments(args);

        GameDataProvider dataProvider = new SteamApiClient();
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
        String token = getTelegramToken();
        String botUsername = getBotUsername(token);

        if (token == null || token.trim().isEmpty()) {
            System.err.println("ОШИБКА: Токен Telegram бота не найден!");
            System.err.println("Доступные способы настройки токена:");
            System.err.println("1. Создайте файл .env в корне проекта с содержимым:");
            System.err.println("   TELEGRAM_BOT_TOKEN=ваш_токен_здесь");
            System.err.println("2. Установите переменную окружения:");
            System.err.println("   export TELEGRAM_BOT_TOKEN=ваш_токен_здесь");
            return;
        }

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBotAdapter telegramBot = new TelegramBotAdapter(processor, botUsername, token);
            botsApi.registerBot(telegramBot);
            System.out.println("Telegram бот успешно запущен!");
            System.out.println("Имя бота: @" + botUsername);
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
    private static String getTelegramToken() {
        String token = System.getenv("TELEGRAM_BOT_TOKEN");
        if (token != null && !token.trim().isEmpty()) {
            System.out.println("Токен найден в переменных окружения");
            return token.trim();
        }

        String[] possiblePaths = {
                ".env",
                "../.env",
                "C:\\Users\\sweank\\IdeaProjects\\steam-price-bot\\.env"
        };

        for (String path : possiblePaths) {
            try {
                Path envFile = Paths.get(path);
                System.out.println("Проверяем: " + envFile.toAbsolutePath());

                if (Files.exists(envFile)) {
                    System.out.println(".env файл найден: " + envFile.toAbsolutePath());
                    List<String> lines = Files.readAllLines(envFile, StandardCharsets.UTF_8);

                    for (String line : lines) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }
                        if (line.startsWith("TELEGRAM_BOT_TOKEN=")) {
                            String foundToken = line.substring("TELEGRAM_BOT_TOKEN=".length()).trim();
                            if (foundToken.startsWith("\"") && foundToken.endsWith("\"")) {
                                foundToken = foundToken.substring(1, foundToken.length() - 1);
                            }
                            return foundToken;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка чтения " + path + ": " + e.getMessage());
            }
        }

        System.out.println(" .env файл не найден ни в одном из мест:");
        for (String path : possiblePaths) {
            System.out.println("   - " + Paths.get(path).toAbsolutePath());
        }

        return null;

    }

    private static String getBotUsername(String token) {
        if (token != null && token.contains(":")) {
            return token.split(":")[0] + "_bot";
        }
        return "steam_price_bot";
    }

    private static String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}