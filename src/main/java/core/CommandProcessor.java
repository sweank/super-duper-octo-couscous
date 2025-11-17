package core;

public class CommandProcessor {
    private final GameSearchService gameService;

    public CommandProcessor(GameSearchService gameService) {
        this.gameService = gameService;
    }

    public String processCommand(String command, String argument) {
        switch (command) {
            case "search":
                return gameService.searchGame(argument);
            case "info":
                try {
                    int appId = Integer.parseInt(argument);
                    return gameService.getGameInfo(appId);
                } catch (NumberFormatException e) {
                    return "❌ AppID должен быть числом!";
                }
            case "help":
                return gameService.getHelpMessage();
            case "start":
                return gameService.getWelcomeMessage();
            default:
                return "❌ Неизвестная команда";
        }
    }
}