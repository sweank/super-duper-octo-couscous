package implementations;

import core.CommandProcessor;
import core.TelegramInputParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramBotAdapter extends TelegramLongPollingBot {
    private final CommandProcessor processor;
    private final TelegramInputParser inputParser;
    private final String botUsername;
    private final String botToken;
    private final Map<Long, String> userStates;

    public TelegramBotAdapter(CommandProcessor processor, String username, String token) {
        this.processor = processor;
        this.inputParser = new TelegramInputParser();
        this.botUsername = username;
        this.botToken = token;
        this.userStates = new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        try {
            if (isButtonClick(messageText)) {
                handleButtonClick(chatId, messageText);
                return;
            }

            handleRegularMessage(chatId, messageText);

        } catch (Exception e) {
            sendTextMessage(chatId, "Ошибка: " + e.getMessage(), false);
        }
    }

    private boolean isButtonClick(String text) {
        return text.equals("Поиск игры") ||
                text.equals("Информация по ID") ||
                text.equals("Помощь") ||
                text.equals("Главное меню");
    }

    private void handleButtonClick(Long chatId, String buttonText) {
        switch (buttonText) {
            case "Поиск игры":
                userStates.put(chatId, "waiting_for_search");
                sendTextMessage(chatId, "Введите название игры для поиска:", false);
                break;

            case "Информация по ID":
                userStates.put(chatId, "waiting_for_appid");
                sendTextMessage(chatId, "Введите AppID игры:", false);
                break;

            case "Помощь":
                String helpResponse = processor.processCommand("help", "");
                sendResponse(chatId, helpResponse, "help");
                break;

            case "Главное меню":
                String startResponse = processor.processCommand("start", "");
                sendResponse(chatId, startResponse, "start");
                break;
        }
    }

    private void handleRegularMessage(Long chatId, String messageText) {
        String userState = userStates.get(chatId);
        if (userState != null) {
            handleUserInput(chatId, messageText, userState);
            userStates.remove(chatId);
            return;
        }

        TelegramInputParser.ParsedCommand parsedCommand = inputParser.parse(messageText);
        String response = processor.processCommand(parsedCommand.getCommand(), parsedCommand.getArgument());
        sendResponse(chatId, response, parsedCommand.getCommand());
    }

    private void handleUserInput(Long chatId, String userInput, String state) {
        try {
            String response;
            if (state.equals("waiting_for_search")) {
                response = processor.processCommand("search", userInput);
                sendResponse(chatId, response, "search");
            } else if (state.equals("waiting_for_appid")) {
                response = processor.processCommand("info", userInput);
                sendResponse(chatId, response, "info");
            }
        } catch (Exception e) {
            sendTextMessage(chatId, "Ошибка обработки ввода: " + e.getMessage(), false);
        }
    }

    private void sendResponse(Long chatId, String response, String command) {
        if (response == null || response.isEmpty()) {
            return;
        }

        boolean showKeyboard = command.equals("start") || command.equals("help");

        if (response.contains("IMAGE_URL:")) {
            String[] parts = response.split("IMAGE_URL:", 2);
            String caption = parts[0].trim();
            String imageUrl = parts[1].split("\n")[0].trim();

            sendPhotoWithCaption(chatId, imageUrl, caption, showKeyboard);
        } else {
            sendTextMessage(chatId, response, showKeyboard);
        }
    }

    private void sendTextMessage(Long chatId, String text, boolean showKeyboard) {
        if (text == null || text.isEmpty()) {
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.enableMarkdown(true);

        if (showKeyboard) {
            message.setReplyMarkup(createMainKeyboard());
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    private void sendPhotoWithCaption(Long chatId, String imageUrl, String caption, boolean showKeyboard) {
        try {
            SendPhoto photo = new SendPhoto();
            photo.setChatId(chatId.toString());
            photo.setPhoto(new InputFile(imageUrl));
            photo.setCaption(caption);
            photo.setParseMode("Markdown");

            if (showKeyboard) {
                photo.setReplyMarkup(createMainKeyboard());
            }

            execute(photo);
        } catch (Exception e) {
            System.err.println("Ошибка отправки фото: " + e.getMessage());
            sendTextMessage(chatId, caption + "\n\nНе удалось загрузить изображение", showKeyboard);
        }
    }

    private ReplyKeyboardMarkup createMainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Поиск игры");
        row1.add("Информация по ID");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Помощь");
        row2.add("Главное меню");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
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