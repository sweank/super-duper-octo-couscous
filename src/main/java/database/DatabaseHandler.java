package database;

import config.ConfigLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public DatabaseHandler() {
        loadCredentials();
        initTable();
    }

    private void loadCredentials() {
        this.dbUrl = ConfigLoader.get("DB_URL");
        this.dbUser = ConfigLoader.get("DB_USER");
        this.dbPass = ConfigLoader.get("DB_PASSWORD");

        if (dbUrl == null || dbPass == null) {
            System.err.println("ОШИБКА: Не найдены настройки БД (DB_URL или DB_PASSWORD). Проверьте .env файл.");
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    private void initTable() {
        if (dbUrl == null) return;

        String sql = "CREATE TABLE IF NOT EXISTS user_search_history (" +
                "id UUID PRIMARY KEY DEFAULT gen_random_uuid(), " +
                "user_id INT8, " +
                "search_query TEXT, " +
                "search_type TEXT, " +
                "created_at TIMESTAMP DEFAULT now()" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("База данных успешно подключена.");
        } catch (SQLException e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
        }
    }

    public void saveSearch(long userId, String query, String type) {
        if (dbUrl == null) return;

        String sql = "INSERT INTO user_search_history (user_id, search_query, search_type) VALUES (?, ?, ?)";

        new Thread(() -> {
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, userId);
                pstmt.setString(2, query);
                pstmt.setString(3, type);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Ошибка сохранения в историю: " + e.getMessage());
            }
        }).start();
    }

    public List<String> getUserHistory(long userId, String filterQuery, int limit) {
        List<String> history = new ArrayList<>();

        if (dbUrl == null) {
            history.add("Ошибка: Нет подключения к базе данных.");
            return history;
        }

        String sql;
        if (filterQuery == null || filterQuery.trim().isEmpty()) {
            sql = "SELECT search_query, search_type FROM user_search_history WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        } else {
            sql = "SELECT search_query, search_type FROM user_search_history WHERE user_id = ? AND search_query ILIKE ? ORDER BY created_at DESC LIMIT ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);

            if (filterQuery == null || filterQuery.trim().isEmpty()) {
                pstmt.setInt(2, limit);
            } else {
                pstmt.setString(2, "%" + filterQuery + "%");
                pstmt.setInt(3, limit);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String query = rs.getString("search_query");
                String type = rs.getString("search_type");
                history.add("[" + type + "] " + query);
            }
        } catch (SQLException e) {
            history.add("Ошибка получения истории: " + e.getMessage());
        }
        return history;
    }
}