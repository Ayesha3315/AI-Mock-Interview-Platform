package com.interview.backend.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InterviewHistoryResponseDTO {
    private Integer sessionId;
    private String roleName;
    private BigDecimal totalScore;
    private LocalDateTime completedDate;

    public InterviewHistoryResponseDTO() {
    }

    public InterviewHistoryResponseDTO(Integer sessionId, String roleName, BigDecimal totalScore, LocalDateTime completedDate) {
        this.sessionId = sessionId;
        this.roleName = roleName;
        this.totalScore = totalScore;
        this.completedDate = completedDate;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
}
