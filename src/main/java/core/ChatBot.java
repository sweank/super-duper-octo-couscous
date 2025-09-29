package core;

import interfaces.IMessenger;
import interfaces.IQuestionRepository;

public class ChatBot {
    private final IQuestionRepository repository;
    private final IMessenger messenger;

    public ChatBot(IQuestionRepository repository, IMessenger messenger) {
        this.repository = repository;
        this.messenger = messenger;
    }

    public void start() {
        messenger.sendMessage("Привет! Я бот для игры в угадывание столицы");
        messenger.sendMessage("Я буду называть случайную страну, а ты — её столицу.");
        messenger.sendMessage("Для выхода в любой момент введи \\quit , для помощи \\help");
        loop:
        while (repository.hasMoreQuestions()) {
            messenger.sendMessage(repository.getQuestion());
            String userAnswer = messenger.receiveMessage();
            switch (userAnswer) {
                case "\\help":
                    messenger.sendMessage("Help");
                    break;
                case "\\quit":
                    break loop;
                default:
                    if (repository.checkAnswer(userAnswer)) {
                        messenger.sendMessage("Верно!");
                    } else {
                        messenger.sendMessage("Неверно!.");
                    }
                    break;
            }
        }

        messenger.sendMessage("Спасибо за игру!");
    }
}