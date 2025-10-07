package implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CapitalQuestionRepositoryTest {

    private CapitalQuestionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CapitalQuestionRepository();
    }

    @Test
    @DisplayName("hasMoreQuestions() всегда должен возвращать true")
    void hasMoreQuestions_ShouldAlwaysReturnTrue() {
        assertTrue(repository.hasMoreQuestions());
    }

    @Test
    @DisplayName("getQuestion() должен возвращать вопрос в формате 'Назовите столицу страны: ...'")
    void getQuestion_ShouldReturnFormattedQuestion() {
        String question = repository.getQuestion();

        assertNotNull(question, "Вопрос не должен быть null.");
        assertTrue(question.startsWith("Назовите столицу страны: "), "Формат вопроса некорректен.");
    }

    @Test
    @DisplayName("checkAnswer() должен возвращать false, если вопрос еще не задавался")
    void checkAnswer_ShouldReturnFalseIfQuestionWasNotAsked() {
        assertFalse(repository.checkAnswer("Москва"), "Проверка ответа до задания вопроса должна возвращать false.");
    }

    @RepeatedTest(10)
    @DisplayName("checkAnswer() должен возвращать true для правильного ответа и false для неправильного")
    void checkAnswer_ShouldCorrectlyValidateAnswer() throws NoSuchFieldException, IllegalAccessException {
        String question = repository.getQuestion();


        Field currentCountryField = CapitalQuestionRepository.class.getDeclaredField("currentCountry");
        currentCountryField.setAccessible(true);
        String currentCountry = (String) currentCountryField.get(repository);

        Field capitalsField = CapitalQuestionRepository.class.getDeclaredField("capitals");
        capitalsField.setAccessible(true);
        Map<String, String> capitals = (Map<String, String>) capitalsField.get(repository);
        String correctAnswer = capitals.get(currentCountry);

        assertTrue(repository.checkAnswer(correctAnswer), "Правильный ответ должен быть принят.");
        assertTrue(repository.checkAnswer(correctAnswer.toLowerCase()), "Ответ в нижнем регистре должен быть принят.");
        assertTrue(repository.checkAnswer(correctAnswer.toUpperCase()), "Ответ в верхнем регистре должен быть принят.");
        assertFalse(repository.checkAnswer("абсолютно неверный ответ"), "Неправильный ответ не должен быть принят.");
    }
}