package core;

import interfaces.GameDataProvider;

public class GameSearchService {
    private final GameDataProvider dataProvider;

    public GameSearchService(GameDataProvider dataProvider) {
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
                "Поиск по названию\n" +
                "Узнать цену\n" +
                "Получить подробности";
    }

    public String getHelpMessage() {
        return "Доступные команды:\n\n" +
                "/search Название - поиск игры\n" +
                "/info AppID - информация об игре\n" +
                "/help - эта справка\n" +
                "/quit - выход\n\n" +
                "Примеры:\n" +
                "/search Counter-Strike\n" +
                "/info 730\n\n";
    }
}