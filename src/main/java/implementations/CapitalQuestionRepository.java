package implementations;

import core.Question;
import interfaces.QuestionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CapitalQuestionRepository implements QuestionRepository {
    private final Map<String, String> capitals = new HashMap<>();
    private final List<String> countries;
    private final Random random = new Random();

    public CapitalQuestionRepository() {
        capitals.put("Россия", "Москва");
        capitals.put("Франция", "Париж");
        capitals.put("Япония", "Токио");
        capitals.put("Австралия", "Канберра");
        capitals.put("Германия", "Берлин");
        capitals.put("Италия", "Рим");

        countries = new ArrayList<>(capitals.keySet());
    }

    @Override
    public boolean hasMoreQuestions() {
        return true;
    }

    @Override
    public Question getQuestion() {
        int randomIndex = random.nextInt(countries.size());
        String currentCountry = countries.get(randomIndex);
        String questionText = "Назовите столицу страны: " + currentCountry;
        String correctAnswer = capitals.get(currentCountry);

        return new Question(questionText, correctAnswer);
    }
}