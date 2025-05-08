package LMS.LearningManagementSystem.model;

public class StudentAnswer {
    private Integer questionId;  // ID of the question
    private String answer;       // Student's answer

    // Getters and Setters

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}