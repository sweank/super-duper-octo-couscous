package interfaces;

import core.Question;

public interface QuestionRepository {
    boolean hasMoreQuestions();

    Question getQuestion();

}