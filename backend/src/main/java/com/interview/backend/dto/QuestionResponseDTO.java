package com.interview.backend.dto;

public class QuestionResponseDTO {
    private Integer questionId;
    private String questionText;

    public QuestionResponseDTO() {
    }

    public QuestionResponseDTO(Integer questionId, String questionText) {
        this.questionId = questionId;
        this.questionText = questionText;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
