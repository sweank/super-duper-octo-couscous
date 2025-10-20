import core.ChatBot;
import implementations.CapitalQuestionRepository;
import implementations.ConsoleMessenger;
import interfaces.Messenger;
import interfaces.QuestionRepository;

public class Main {
    public static void main(String[] args) {
        QuestionRepository repo = new CapitalQuestionRepository();
        Messenger messenger = new ConsoleMessenger();

        ChatBot bot = new ChatBot(repo, messenger);

        bot.start();
    }
}