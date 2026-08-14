package com.interview.backend.dto;


public class AnswerSubmissionRequestDTO {
    private Integer sessionId;
    private Integer questionId;
    private String userAnswer;

    public AnswerSubmissionRequestDTO() {
    }

    public AnswerSubmissionRequestDTO(Integer sessionId, Integer questionId, String userAnswer) {
        this.sessionId = sessionId;
        this.questionId = questionId;
        this.userAnswer = userAnswer;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
