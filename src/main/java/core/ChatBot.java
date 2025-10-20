package core;

import interfaces.Messenger;
import interfaces.QuestionRepository;

public class ChatBot {
    private final QuestionRepository repository;
    private final Messenger messenger;

    public ChatBot(QuestionRepository repository, Messenger messenger) {
        this.repository = repository;
        this.messenger = messenger;
    }

    public void start() {
        messenger.sendMessage("Привет! Я бот для игры в угадывание столицы");
        messenger.sendMessage("Я буду называть случайную страну, а ты — её столицу.");
        messenger.sendMessage("Для выхода в любой момент введите \\quit , для помощи \\help");

        while (repository.hasMoreQuestions()) {
            Question currentQuestion = repository.getQuestion();

            while (true) {
                messenger.sendMessage(currentQuestion.getQuestionText());
                String userAnswer = messenger.receiveMessage();

                if ("\\help".equals(userAnswer)) {
                    messenger.sendMessage(
                            "Я задаю страну, а ты должен угадать её столицу.\n" +
                                    "\\quit - выход из игры\n" +
                                    "\\help - показать эту справку"
                    );
                    continue;
                }

                if ("\\quit".equals(userAnswer)) {
                    messenger.sendMessage("Спасибо за игру!");
                    return;
                }

                if (userAnswer != null && userAnswer.equalsIgnoreCase(currentQuestion.getAnswer())) {
                    messenger.sendMessage("Верно!");
                } else {
                    messenger.sendMessage("Неверно! Правильный ответ: " + currentQuestion.getAnswer());
                }
                break;
            }
        }
    }
}