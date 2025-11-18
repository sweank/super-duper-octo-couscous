package commands;

import core.GameSearchService;

public class InfoCommand implements Command {
    private final GameSearchService gameService;

    public InfoCommand(GameSearchService gameService) {
        this.gameService = gameService;
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
            return gameService.getGameInfo(appId);
        } catch (NumberFormatException e) {
            return "AppID должен быть числом!";
        }
    }
}