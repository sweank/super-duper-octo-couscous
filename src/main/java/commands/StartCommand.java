package commands;

import core.GameSearchService;

public class StartCommand implements Command {
    private final GameSearchService gameService;

    public StartCommand(GameSearchService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "начало работы с ботом";
    }

    @Override
    public String execute(long userId, String argument) {
        return gameService.getWelcomeMessage();
    }
}