package implementations;

import core.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CapitalQuestionRepositoryTest {

    private CapitalQuestionRepository repository;
    private Map<String, String> capitalsFromImplementation;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        repository = new CapitalQuestionRepository();

        Field capitalsField = CapitalQuestionRepository.class.getDeclaredField("capitals");
        capitalsField.setAccessible(true);
        capitalsFromImplementation = (Map<String, String>) capitalsField.get(repository);
    }

    @Test
    @DisplayName("hasMoreQuestions() всегда должен возвращать true")
    void hasMoreQuestions_ShouldAlwaysReturnTrue() {
        assertTrue(repository.hasMoreQuestions(), "Метод hasMoreQuestions должен всегда возвращать true.");
    }

    @RepeatedTest(20)
    @DisplayName("getQuestion() должен возвращать корректный объект Question")
    void getQuestion_ShouldReturnCorrectQuestionObject() {
        Question q = repository.getQuestion();

        assertNotNull(q, "Объект Question не должен быть null.");
        assertNotNull(q.getQuestionText(), "Текст вопроса не должен быть null.");
        assertNotNull(q.getAnswer(), "Ответ не должен быть null.");

        String prefix = "Назовите столицу страны: ";
        assertTrue(q.getQuestionText().startsWith(prefix), "Формат вопроса некорректен.");


        String country = q.getQuestionText().replace(prefix, "");

        assertTrue(capitalsFromImplementation.containsKey(country), "Сгенерирована неизвестная страна: " + country);

        String expectedAnswer = capitalsFromImplementation.get(country);

        assertEquals(expectedAnswer, q.getAnswer(), "Для страны '" + country + "' указан неверный ответ.");
    }
}