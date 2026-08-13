package com.interview.backend.dto;


import java.math.BigDecimal;

public class EvaluatedQuestionDTO {
    private String questionText;
    private String userAnswer;
    private String idealAnswer;
    private BigDecimal similarityScore;
    private String remark;
    public EvaluatedQuestionDTO() {
    }

    public EvaluatedQuestionDTO(String questionText, String userAnswer, String idealAnswer, BigDecimal similarityScore, String remark) {
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.idealAnswer = idealAnswer;
        this.similarityScore = similarityScore;
        this.remark = remark;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getIdealAnswer() {
        return idealAnswer;
    }

    public void setIdealAnswer(String idealAnswer) {
        this.idealAnswer = idealAnswer;
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
}

