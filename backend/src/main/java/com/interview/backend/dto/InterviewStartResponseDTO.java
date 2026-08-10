package com.interview.backend.dto;

public class InterviewStartResponseDTO {
    private Integer sessionId;
    private QuestionResponseDTO firstQuestion;

    public InterviewStartResponseDTO() {
    }

    public InterviewStartResponseDTO(Integer sessionId, QuestionResponseDTO firstQuestion) {
        this.sessionId = sessionId;
        this.firstQuestion = firstQuestion;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public QuestionResponseDTO getFirstQuestion() {
        return firstQuestion;
    }

    public void setFirstQuestion(QuestionResponseDTO firstQuestion) {
        this.firstQuestion = firstQuestion;
    }
}
