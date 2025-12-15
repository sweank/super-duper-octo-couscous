package commands;

import core.GameSearchService;

public class HistoryCommand implements Command {
    private final GameSearchService gameService;

    public HistoryCommand(GameSearchService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public String getDescription() {
        return "история поиска (введите текст после команды для поиска по истории)";
    }

    @Override
    public String execute(long userId, String argument) {
        return gameService.getSearchHistory(userId, argument);
    }
}