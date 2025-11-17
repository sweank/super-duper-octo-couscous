package implementations;

import core.CommandProcessor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotAdapter extends TelegramLongPollingBot {
    private final CommandProcessor processor;
    private String botUsername;
    private String botToken;

    public TelegramBotAdapter(CommandProcessor processor, String username, String token) {
        this.processor = processor;
        this.botUsername = username;
        this.botToken = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            String[] parts = parseTelegramInput(messageText);
            String response = processor.processCommand(parts[0], parts[1]);

            sendMessageToChat(chatId, response);
        }
    }

    private String[] parseTelegramInput(String input) {
        if (input.startsWith("/search ")) {
            return new String[]{"search", input.substring(8)};
        } else if (input.startsWith("/info ")) {
            return new String[]{"info", input.substring(6)};
        } else if (input.startsWith("/")) {
            return new String[]{input.substring(1), ""};
        } else {
            return new String[]{"search", input}; // текст без команды = поиск
        }
    }

    private void sendMessageToChat(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}