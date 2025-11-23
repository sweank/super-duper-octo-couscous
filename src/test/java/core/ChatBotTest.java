package core;

import interfaces.Messenger;
import interfaces.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class ChatBotTest {

    private FakeMessenger fakeMessenger;
    private FakeQuestionRepository fakeRepository;
    private ChatBot bot;

    static class FakeMessenger implements Messenger {
        private final Queue<String> userInputs = new LinkedList<>();
        private final List<String> sentMessages = new ArrayList<>();

        @Override
        public void sendMessage(String text) {
            sentMessages.add(text);
        }

        @Override
        public String receiveMessage() {
            return userInputs.poll();
        }

        public void addUserInput(String input) {
            userInputs.add(input);
        }

        public List<String> getSentMessages() {
            return sentMessages;
        }
    }

    static class FakeQuestionRepository implements QuestionRepository {
        private final Queue<Question> questions = new LinkedList<>();

        @Override
        public boolean hasMoreQuestions() {
            return !questions.isEmpty();
        }

        @Override
        public Question getQuestion() {
            return questions.poll();
        }

        public void addQuestion(String questionText, String correctAnswer) {
            questions.add(new Question(questionText, correctAnswer));
        }
    }


    @BeforeEach
    void setUp() {
        fakeMessenger = new FakeMessenger();
        fakeRepository = new FakeQuestionRepository();
        bot = new ChatBot(fakeRepository, fakeMessenger);
    }

    @Test
    @DisplayName("Бот должен отправить только приветственные сообщения, если нет вопросов")
    void start_ShouldSendWelcomeMessagesIfNoQuestions() {
        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertEquals(3, messages.size());
        assertEquals("Привет! Я бот для игры в угадывание столицы", messages.get(0));
        assertEquals("Я буду называть случайную страну, а ты — её столицу.", messages.get(1));
        assertEquals("Для выхода в любой момент введите \\quit , для помощи \\help", messages.get(2));
    }

    @Test
    @DisplayName("Бот должен корректно обработать цикл с правильным ответом")
    void start_ShouldHandleOneCycleWithCorrectAnswer() {
        fakeRepository.addQuestion("Назовите столицу Франции", "Париж");
        fakeMessenger.addUserInput("Париж");

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.contains("Назовите столицу Франции"), "Бот должен был задать вопрос.");
        assertTrue(messages.contains("Верно!"), "Бот должен был сообщить о верном ответе.");
    }

    @Test
    @DisplayName("Бот должен корректно обработать цикл с неправильным ответом")
    void start_ShouldHandleOneCycleWithIncorrectAnswer() {
        fakeRepository.addQuestion("Назовите столицу Японии", "Токио");
        fakeMessenger.addUserInput("Киото");

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.contains("Назовите столицу Японии"));
        assertTrue(messages.contains("Неверно! Правильный ответ: Токио"));
        assertFalse(messages.contains("Верно!"), "Сообщения о верном ответе быть не должно.");
    }

    @Test
    @DisplayName("Бот должен завершать работу и прощаться по команде \\quit")
    void start_ShouldStopOnQuitCommand() {
        fakeRepository.addQuestion("Вопрос 1", "Ответ 1");
        fakeMessenger.addUserInput("\\quit");

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.contains("Вопрос 1"));
        assertEquals("Спасибо за игру!", messages.get(messages.size() - 1));
        assertFalse(messages.stream().anyMatch(msg -> msg.startsWith("Верно") || msg.startsWith("Неверно")));
    }

    @Test
    @DisplayName("Бот должен выводить помощь по команде \\help и продолжать игру")
    void start_ShouldShowHelpOnHelpCommandAndContinue() {
        fakeRepository.addQuestion("Вопрос 1", "Ответ 1");
        fakeRepository.addQuestion("Вопрос 2", "Ответ 2");
        fakeMessenger.addUserInput("\\help");
        fakeMessenger.addUserInput("Ответ 1");

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.stream().anyMatch(msg -> msg.contains("Я задаю страну")), "Бот должен был показать сообщение помощи.");
        assertEquals(2, messages.stream().filter(msg -> msg.equals("Вопрос 1")).count());
        assertTrue(messages.contains("Верно!"), "Не было реакции на правильный ответ после \\help.");
        assertTrue(messages.contains("Вопрос 2"), "Бот не задал следующий вопрос.");
    }
}