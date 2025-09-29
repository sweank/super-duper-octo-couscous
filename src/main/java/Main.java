import core.ChatBot;
import implementations.CapitalQuestionRepository;
import implementations.ConsoleMessenger;
import interfaces.IMessenger;
import interfaces.IQuestionRepository;


public class Main {
    public static void main(String[] args) {
        IQuestionRepository repo = new CapitalQuestionRepository();
        IMessenger messenger = new ConsoleMessenger();

        ChatBot bot = new ChatBot(repo, messenger);

        bot.start();
    }
}