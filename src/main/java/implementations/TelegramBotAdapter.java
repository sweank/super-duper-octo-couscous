package implementations;

import core.CommandProcessor;
import core.TelegramInputParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotAdapter extends TelegramLongPollingBot {
    private final CommandProcessor processor;
    private final TelegramInputParser inputParser;
    private String botUsername;
    private String botToken;

    public TelegramBotAdapter(CommandProcessor processor, String username, String token) {
        this.processor = processor;
        this.inputParser = new TelegramInputParser();
        this.botUsername = username;
        this.botToken = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            TelegramInputParser.ParsedCommand parsedCommand = inputParser.parse(messageText);
            String response = processor.processCommand(parsedCommand.getCommand(), parsedCommand.getArgument());

            sendMessageToChat(chatId, response);
        }
    }

    private void sendMessageToChat(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.enableMarkdown(true);

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