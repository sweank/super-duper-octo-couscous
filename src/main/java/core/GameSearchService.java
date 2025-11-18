package core;

import interfaces.IGameDataProvider;

public class GameSearchService {
    private final IGameDataProvider dataProvider;

    public GameSearchService(IGameDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String searchGame(String gameName) {
        try {
            if (gameName.length() < 2) {
                return "Введите минимум 2 символа для поиска.";
            }
            return dataProvider.searchGame(gameName);
        } catch (Exception e) {
            return "Ошибка при поиске: " + e.getMessage();
        }
    }

    public String getGameInfo(int appId) {
        try {
            return dataProvider.getGameInfo(appId);
        } catch (Exception e) {
            return "Ошибка при получении информации: " + e.getMessage();
        }
    }

    public String getWelcomeMessage() {
        return "Добро пожаловать в Steam Price Bot!\n\n" +
                "Я помогу вам найти информацию об играх Steam:\n" +
                "- Поиск по названию\n" +
                "- Узнать цену\n" +
                "- Получить подробности\n\n" +
                "Используйте команды для взаимодействия!";
    }

    public String getHelpMessage() {
        return "Справка по командам (консольная версия):\n\n" +
                "- search [название] - поиск игры\n" +
                "- info [AppID] - информация об игре\n" +
                "- help - справка по командам\n" +
                "- start - показать приветствие\n" +
                "- quit - выход из программы\n\n" +
                "Примеры использования:\n" +
                "search Counter-Strike\n" +
                "info 730\n" +
                "help";
    }
}