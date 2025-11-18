package commands;

import core.GameSearchService;

public class SearchCommand implements Command {
    private final GameSearchService gameService;

    public SearchCommand(GameSearchService gameService) {
        this.gameService = gameService;
    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String getDescription() {
        return "поиск игры по названию";
    }

    @Override
    public String execute(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            return "Укажите название игры после команды /search";
        }
        return gameService.searchGame(argument.trim());
    }
}