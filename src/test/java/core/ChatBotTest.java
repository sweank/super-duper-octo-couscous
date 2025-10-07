package core;

import interfaces.IMessenger;
import interfaces.IQuestionRepository;
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

    static class FakeMessenger implements IMessenger {
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

    static class FakeQuestionRepository implements IQuestionRepository {
        private final Queue<String> questions = new LinkedList<>();
        private final Queue<String> correctAnswers = new LinkedList<>();
        private String currentCorrectAnswer;

        @Override
        public boolean hasMoreQuestions() {
            return !questions.isEmpty();
        }

        @Override
        public String getQuestion() {
            currentCorrectAnswer = correctAnswers.poll();
            return questions.poll();
        }

        @Override
        public boolean checkAnswer(String userAnswer) {
            return userAnswer.equalsIgnoreCase(currentCorrectAnswer);
        }

        public void addQuestion(String question, String correctAnswer) {
            questions.add(question);
            correctAnswers.add(correctAnswer);
        }
    }


    @BeforeEach
    void setUp() {
        fakeMessenger = new FakeMessenger();
        fakeRepository = new FakeQuestionRepository();
        bot = new ChatBot(fakeRepository, fakeMessenger);
    }

    @Test
    @DisplayName("Бот должен отправить приветственные и прощальное сообщения на старте, если нет вопросов")
    void start_ShouldSendWelcomeAndGoodbyeMessagesIfNoQuestions() {

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertEquals(4, messages.size());
        assertEquals("Привет! Я бот для игры в угадывание столицы", messages.get(0));
        assertEquals("Я буду называть случайную страну, а ты — её столицу.", messages.get(1));
        assertEquals("Для выхода в любой момент введите \\quit , для помощи \\help", messages.get(2));
        assertEquals("Спасибо за игру!", messages.get(3));
    }

    @Test
    @DisplayName("Бот должен корректно обработать один цикл игры с правильным ответом")
    void start_ShouldHandleOneCycleWithCorrectAnswer() {
        fakeRepository.addQuestion("Назовите столицу Франции", "Париж");
        fakeMessenger.addUserInput("Париж");

        bot.start();


        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.contains("Назовите столицу Франции"), "Бот должен был задать вопрос.");
        assertTrue(messages.contains("Верно!"), "Бот должен был сообщить о верном ответе.");
        assertTrue(messages.contains("Спасибо за игру!"), "Бот должен был попрощаться в конце.");
    }

    @Test
    @DisplayName("Бот должен корректно обработать один цикл игры с неправильным ответом")
    void start_ShouldHandleOneCycleWithIncorrectAnswer() {
        fakeRepository.addQuestion("Назовите столицу Японии", "Токио");
        fakeMessenger.addUserInput("Киото");

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.contains("Назовите столицу Японии"));
        assertTrue(messages.contains("Неверно!."));
        assertFalse(messages.contains("Верно!"), "Сообщения о верном ответе быть не должно.");
    }

    @Test
    @DisplayName("Бот должен завершать работу по команде \\quit")
    void start_ShouldStopOnQuitCommand() {
        fakeRepository.addQuestion("Вопрос 1", "Ответ 1");
        fakeMessenger.addUserInput("\\quit");

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.contains("Вопрос 1"));
        assertFalse(messages.contains("Верно!"));
        assertFalse(messages.contains("Неверно!."));
        assertEquals("Спасибо за игру!", messages.get(messages.size() - 1));
    }

    @Test
    @DisplayName("Бот должен выводить помощь по команде \\help")
    void start_ShouldShowHelpOnHelpCommand() {
        fakeRepository.addQuestion("Вопрос 1", "Ответ 1");
        fakeMessenger.addUserInput("\\help");

        bot.start();

        List<String> messages = fakeMessenger.getSentMessages();
        assertTrue(messages.contains("Help"), "Бот должен был показать сообщение помощи.");
        assertFalse(messages.contains("Верно!"), "Не должно быть реакции на ответ.");
        assertFalse(messages.contains("Неверно!."), "Не должно быть реакции на ответ.");
    }
}