package core;

import interfaces.IGameDataProvider;
import interfaces.IMessenger;

public class SteamBot {
    private final IGameDataProvider dataProvider;
    private final IMessenger messenger;
    private boolean running = true;
    private final String botName;

    public SteamBot(IGameDataProvider dataProvider, IMessenger messenger) {
        this.dataProvider = dataProvider;
        this.messenger = messenger;
        this.botName = messenger.getClass().getSimpleName();
    }

    public void start() {
        messenger.sendMessage("🎮 Привет! Я Steam Price Bot! (" + botName + ")");
        messenger.sendMessage("Просто отправь мне название игры или используй команды:");
        messenger.sendMessage("/search [название] - найти игру");
        messenger.sendMessage("/info [AppID] - информация об игре");
        messenger.sendMessage("/help - помощь");
        messenger.sendMessage("/quit - выйти в главное меню");

        while (running) {
            String userInput = messenger.receiveMessage().trim();

            if (userInput.startsWith("/search ")) {
                searchGame(userInput);
            } else if (userInput.startsWith("/info ")) {
                getGameInfo(userInput);
            } else if (userInput.equals("/help")) {
                showHelp();
            } else if (userInput.equals("/start")) {
                showWelcome();
            } else if (userInput.equals("/quit")) {
                quitToMainMenu();
            } else if (userInput.startsWith("/")) {
                messenger.sendMessage("❌ Неизвестная команда. Используй /help для списка команд");
            } else {
                searchGame("/search " + userInput);
            }
        }
    }

    private void showWelcome() {
        messenger.sendMessage("🎮 Привет! Я Steam Price Bot! (" + botName + ")");
        showHelp();
    }

    private void showHelp() {
        messenger.sendMessage("📋 Помощь по командам:");
        messenger.sendMessage("/search [название] - найти игру");
        messenger.sendMessage("/info [AppID] - получить информацию об игре по ID");
        messenger.sendMessage("/help - показать эту справку");
        messenger.sendMessage("/quit - выйти в главное меню");
        messenger.sendMessage("\n📝 Примеры:");
        messenger.sendMessage("/search Counter-Strike");
        messenger.sendMessage("/info 730");
        messenger.sendMessage("\n💡 Или просто отправь название игры для поиска");
    }

    private void searchGame(String userInput) {
        try {
            String gameName = userInput.substring("/search ".length()).trim();
            if (gameName.isEmpty()) {
                messenger.sendMessage("❌ Укажите название игры после команды /search");
                return;
            }

            messenger.sendMessage("🔍 Ищем игру: " + gameName + "...");
            String result = dataProvider.searchGame(gameName);
            messenger.sendMessage(result);

        } catch (Exception e) {
            messenger.sendMessage("❌ Ошибка при поиске игры: " + e.getMessage());
        }
    }

    private void getGameInfo(String userInput) {
        try {
            String appIdStr = userInput.substring("/info ".length()).trim();
            if (appIdStr.isEmpty()) {
                messenger.sendMessage("❌ Укажите AppID после команды /info");
                return;
            }

            int appId = Integer.parseInt(appIdStr);
            messenger.sendMessage("📊 Загружаем информацию об игре...");
            String gameInfo = dataProvider.getGameInfo(appId);
            messenger.sendMessage(gameInfo);

        } catch (NumberFormatException e) {
            messenger.sendMessage("❌ AppID должен быть числом!");
        } catch (Exception e) {
            messenger.sendMessage("❌ Ошибка при получении информации: " + e.getMessage());
        }
    }

    private void quitToMainMenu() {
        messenger.sendMessage("🔄 Выход в главное меню...");
        running = false;
    }
}