package config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EnvironmentConfig {

    public static String getTelegramBotToken() {
        String token = System.getenv("TELEGRAM_BOT_TOKEN");
        if (token != null && !token.trim().isEmpty()) {
            return token.trim();
        }

        token = readTokenFromEnvFile();
        if (token != null && !token.trim().isEmpty()) {
            return token.trim();
        }

        throw new IllegalStateException("TELEGRAM_BOT_TOKEN не найден!");
    }

    private static String readTokenFromEnvFile() {
        try {
            Path projectRoot = Paths.get("").toAbsolutePath();
            Path envPath = projectRoot.resolve(".env");

            if (!envPath.toFile().exists()) {
                System.err.println("Файл .env не найден по пути: " + envPath);
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(envPath.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("TELEGRAM_BOT_TOKEN=")) {
                        String token = line.substring("TELEGRAM_BOT_TOKEN=".length());
                        System.out.println("Токен найден в .env файле");
                        return token;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения .env файла: " + e.getMessage());
        }
        return null;
    }

    public static boolean isTelegramTokenAvailable() {
        try {
            getTelegramBotToken();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}