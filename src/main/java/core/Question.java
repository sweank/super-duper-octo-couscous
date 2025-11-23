package core;

public class Question {
    private final String questionText;
    private final String answer;

    public Question(String questionText, String answer) {
        this.questionText = questionText;
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }
}