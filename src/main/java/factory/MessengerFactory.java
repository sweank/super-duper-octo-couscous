package factory;

import enums.MessengerType;
import implementations.ConsoleMessenger;
import implementations.TelegramMessenger;
import interfaces.IMessenger;

public class MessengerFactory {
    public static IMessenger createMessenger(MessengerType type, String... params) {
        switch (type) {
            case TELEGRAM:
                if (params.length >= 2) {
                    TelegramMessenger telegramMessenger = new TelegramMessenger();
                    telegramMessenger.setBotUsername(params[0]);
                    telegramMessenger.setBotToken(params[1]);
                    return telegramMessenger;
                } else {
                    throw new IllegalArgumentException("Для Telegram нужны username и token");
                }
            case CONSOLE:
            default:
                return new ConsoleMessenger();
        }
    }
}