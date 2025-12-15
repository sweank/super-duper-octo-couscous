package config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigLoader {

    public static void load() {
        String[] possiblePaths = { ".env", "../.env" };

        for (String pathString : possiblePaths) {
            try {
                Path path = Paths.get(pathString);
                if (Files.exists(path)) {
                    System.out.println("[Config] Загрузка настроек из: " + path.toAbsolutePath());
                    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

                    for (String line : lines) {
                        parseLine(line);
                    }
                    return;
                }
            } catch (Exception e) {
                System.err.println("[Config] Ошибка чтения .env: " + e.getMessage());
            }
        }
        System.out.println("[Config] Файл .env не найден. Используются системные переменные.");
    }

    private static void parseLine(String line) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) return;

        int splitIndex = line.indexOf('=');
        if (splitIndex > 0) {
            String key = line.substring(0, splitIndex).trim();
            String value = line.substring(splitIndex + 1).trim();

            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            System.setProperty(key, value);
        }
    }


    public static String get(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }
}