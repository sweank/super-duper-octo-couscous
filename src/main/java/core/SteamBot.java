package core;

import interfaces.IGameDataProvider;
import interfaces.IMessenger;

public class SteamBot {
    private final GameSearchService gameService;
    private final IMessenger messenger;
    private boolean running = true;

    public SteamBot(IGameDataProvider dataProvider, IMessenger messenger) {
        this.gameService = new GameSearchService(dataProvider);
        this.messenger = messenger;
    }

    public void start() {
        messenger.sendMessage(gameService.getWelcomeMessage());
        showHelp();

        while (running) {
            String userInput = messenger.receiveMessage().trim();
            processInput(userInput);
        }
    }

    private void processInput(String userInput) {
        if (userInput.startsWith("search ")) {
            searchGame(userInput);
        } else if (userInput.startsWith("info ")) {
            getGameInfo(userInput);
        } else if (userInput.equals("help")) {
            showHelp();
        } else if (userInput.equals("start")) {
            showWelcome();
        } else if (userInput.equals("quit")) {
            quitToMainMenu();
        } else {
            messenger.sendMessage("Неизвестная команда. Используй help для списка команд");
        }
    }

    private void showWelcome() {
        messenger.sendMessage(gameService.getWelcomeMessage());
    }

    private void showHelp() {
        messenger.sendMessage(gameService.getHelpMessage());
    }

    private void searchGame(String userInput) {
        try {
            String gameName = userInput.substring("search ".length()).trim();
            if (gameName.isEmpty()) {
                messenger.sendMessage("Укажите название игры после команды search");
                return;
            }

            messenger.sendMessage("Ищем игру: " + gameName + "...");
            String result = gameService.searchGame(gameName);
            messenger.sendMessage(result);

        } catch (Exception e) {
            messenger.sendMessage("Ошибка при поиске игры: " + e.getMessage());
        }
    }

    private void getGameInfo(String userInput) {
        try {
            String appIdStr = userInput.substring("info ".length()).trim();
            if (appIdStr.isEmpty()) {
                messenger.sendMessage("Укажите AppID после команды info");
                return;
            }

            int appId = Integer.parseInt(appIdStr);
            messenger.sendMessage("Загружаем информацию об игре...");
            String gameInfo = gameService.getGameInfo(appId);
            messenger.sendMessage(gameInfo);

        } catch (NumberFormatException e) {
            messenger.sendMessage("AppID должен быть числом!");
        } catch (Exception e) {
            messenger.sendMessage("Ошибка при получении информации: " + e.getMessage());
        }
    }

    private void quitToMainMenu() {
        messenger.sendMessage("Выход в главное меню...");
        running = false;
    }
}