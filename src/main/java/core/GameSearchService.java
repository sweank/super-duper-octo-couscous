package core;

import database.DatabaseHandler;
import interfaces.GameDataProvider;
import models.GameInfo;
import java.util.List;

public class GameSearchService {
    private final GameDataProvider dataProvider;
    private final DatabaseHandler dbHandler;

    public GameSearchService(GameDataProvider dataProvider, DatabaseHandler dbHandler) {
        this.dataProvider = dataProvider;
        this.dbHandler = dbHandler;
    }

    public String searchGame(long userId, String gameName) {
        try {
            if (gameName.length() < 2) {
                return "Введите минимум 2 символа для поиска.";
            }
            if (dbHandler != null) {
                dbHandler.saveSearch(userId, gameName, "SEARCH");
            }
            return dataProvider.searchGame(gameName);
        } catch (Exception e) {
            return "Ошибка при поиске: " + e.getMessage();
        }
    }

    public String getGameInfo(long userId, int appId) {
        try {
            if (dbHandler != null) {
                dbHandler.saveSearch(userId, String.valueOf(appId), "INFO");
            }
            GameInfo info = dataProvider.getGameInfo(appId);
            return info.formatForConsole();
        } catch (Exception e) {
            return "Ошибка при получении информации: " + e.getMessage();
        }
    }

    public String getSearchHistory(long userId, String filter) {
        if (dbHandler == null) return "База данных недоступна.";

        List<String> history = dbHandler.getUserHistory(userId, filter, 10);

        if (history.isEmpty()) {
            if (filter != null && !filter.isEmpty()) {
                return "В сохраненной истории не найдено записей по запросу: '" + filter + "'";
            }
            return "Ваша история поиска пуста.";
        }

        StringBuilder sb = new StringBuilder();
        if (filter != null && !filter.isEmpty()) {
            sb.append("Результаты поиска в истории ('").append(filter).append("'):\n\n");
        } else {
            sb.append("Ваша история запросов (последние 10):\n\n");
        }

        for (String record : history) {
            sb.append("• ").append(record).append("\n");
        }
        return sb.toString();
    }

    public String getWelcomeMessage() {
        return "Добро пожаловать в Steam Price Bot!\n\n" +
                "Я помогу вам найти информацию об играх Steam:\n" +
                "• Поиск по названию\n" +
                "• Узнать цену\n" +
                "• История поиска (/history [текст])\n\n";
    }

    public String getHelpMessage() {
        return "Доступные команды:\n\n" +
                "/search [название] - поиск игры\n" +
                "/info [AppID] - информация об игре\n" +
                "/history - ваша история поиска\n" +
                "/history [текст] - поиск по сохранённым данным\n" +
                "/help - справка\n" +
                "/quit - выход\n";
    }
}