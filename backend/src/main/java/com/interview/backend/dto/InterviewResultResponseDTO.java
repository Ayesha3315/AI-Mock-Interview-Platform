package com.interview.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class InterviewResultResponseDTO {
    private Integer sessionId;
    private BigDecimal totalScore;
    private String overallRemark;
    private List<EvaluatedQuestionDTO> evaluatedQuestions;

    public InterviewResultResponseDTO() {
    }

    public InterviewResultResponseDTO(Integer sessionId, BigDecimal totalScore, String overallRemark, List<EvaluatedQuestionDTO> evaluatedQuestions) {
        this.sessionId = sessionId;
        this.totalScore = totalScore;
        this.overallRemark = overallRemark;
        this.evaluatedQuestions = evaluatedQuestions;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public String getOverallRemark() {
        return overallRemark;
    }

    public void setOverallRemark(String overallRemark) {
        this.overallRemark = overallRemark;
    }

    public List<EvaluatedQuestionDTO> getEvaluatedQuestions() {
        return evaluatedQuestions;
    }

    public void setEvaluatedQuestions(List<EvaluatedQuestionDTO> evaluatedQuestions) {
        this.evaluatedQuestions = evaluatedQuestions;
    }
}