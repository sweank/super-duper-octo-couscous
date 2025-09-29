package implementations;

import interfaces.IQuestionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CapitalQuestionRepository implements IQuestionRepository {
    private final Map<String, String> capitals = new HashMap<>();
    private final List<String> countries;
    private final Random random = new Random();

    private String currentCountry;

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
    public String getQuestion() {
        int randomIndex = random.nextInt(countries.size());
        this.currentCountry = countries.get(randomIndex);

        return "Назовите столицу страны: " + this.currentCountry;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        if (currentCountry == null) {
            return false;
        }

        String correctAnswer = capitals.get(currentCountry);
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }
}