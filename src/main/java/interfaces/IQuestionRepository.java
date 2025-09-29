package interfaces;

public interface IQuestionRepository {

    boolean hasMoreQuestions();

    String getQuestion();

    boolean checkAnswer(String userAnswer);
}