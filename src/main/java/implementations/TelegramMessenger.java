package implementations;

import interfaces.IMessenger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TelegramMessenger extends TelegramLongPollingBot implements IMessenger {
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private Long currentChatId;
    private String botUsername;
    private String botToken;

    public void setBotUsername(String username) {
        this.botUsername = username;
    }

    public void setBotToken(String token) {
        this.botToken = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            currentChatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            messageQueue.offer(messageText);
        }
    }

    @Override
    public void sendMessage(String text) {
        if (currentChatId != null) {
            SendMessage message = new SendMessage();
            message.setChatId(currentChatId.toString());
            message.setText(text);
            message.enableMarkdown(true);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String receiveMessage() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
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