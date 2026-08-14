package com.interview.backend.dto;

import java.math.BigDecimal;

public class AnswerSubmissionResponseDTO {
    private BigDecimal similarityScore;
    private String remark;
    private boolean completed;
    private QuestionResponseDTO nextQuestion;

    public AnswerSubmissionResponseDTO() {
    }

    public AnswerSubmissionResponseDTO(BigDecimal similarityScore, String remark, boolean completed, QuestionResponseDTO nextQuestion) {
        this.similarityScore = similarityScore;
        this.remark = remark;
        this.completed = completed;
        this.nextQuestion = nextQuestion;
    }

    public BigDecimal getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(BigDecimal similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public QuestionResponseDTO getNextQuestion() {
        return nextQuestion;
    }

    public void setNextQuestion(QuestionResponseDTO nextQuestion) {
        this.nextQuestion = nextQuestion;
    }
}
