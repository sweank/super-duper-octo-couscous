package commands;

import core.GameSearchService;
import implementations.TelegramGameService;

public class InfoCommand implements Command {
    private final GameSearchService gameService;
    private final TelegramGameService telegramGameService;
    private final boolean isConsoleMode;

    public InfoCommand(GameSearchService gameService, TelegramGameService telegramGameService, boolean isConsoleMode) {
        this.gameService = gameService;
        this.telegramGameService = telegramGameService;
        this.isConsoleMode = isConsoleMode;
    }

    public InfoCommand(GameSearchService gameService) {
        this.gameService = gameService;
        this.telegramGameService = null;
        this.isConsoleMode = true;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "информация об игре по AppID";
    }

    @Override
    public String execute(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            return "Укажите AppID после команды /info";
        }

        try {
            int appId = Integer.parseInt(argument.trim());

            if (isConsoleMode || telegramGameService == null) {

                return gameService.getGameInfo(appId);
            } else {

                try {
                    return telegramGameService.getGameInfoWithImage(appId);
                } catch (Exception e) {
                    System.err.println("Ошибка при получении изображения: " + e.getMessage());

                    return telegramGameService.getGameInfoForTelegram(appId);
                }
            }
        } catch (NumberFormatException e) {
            return "AppID должен быть числом!";
        } catch (Exception e) {
            return "Ошибка при получении информации: " + e.getMessage();
        }
    }
}