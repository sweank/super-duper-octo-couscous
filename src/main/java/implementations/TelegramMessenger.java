package implementations;

import interfaces.Messenger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TelegramMessenger extends TelegramLongPollingBot implements Messenger {
    private static final int MAX_QUEUE_SIZE = 100;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
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

            try {
                messageQueue.add(messageText);
            } catch (IllegalStateException e) {
                handleQueueOverflow(messageText);
            }
        }
    }

    private void handleQueueOverflow(String newMessage) {
        System.err.println("Очередь сообщений переполнена. Очищаем и добавляем новое сообщение.");

        messageQueue.clear();

        try {
            messageQueue.add(newMessage);
        } catch (IllegalStateException e) {
            System.err.println("Критическая ошибка: не удалось добавить сообщение после очистки очереди: " + newMessage);
        }
    }

    @Override
    public void sendMessage(String text) {
        if (currentChatId != null) {
            SendMessage message = new SendMessage();
            message.setChatId(currentChatId.toString());
            message.setText(text);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.err.println("Ошибка отправки сообщения в Telegram: " + e.getMessage());
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