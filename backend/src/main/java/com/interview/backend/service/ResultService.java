package com.interview.backend.service;

import com.interview.backend.dto.EvaluatedQuestionDTO;
import com.interview.backend.dto.InterviewHistoryResponseDTO;
import com.interview.backend.dto.InterviewResultResponseDTO;
import com.interview.backend.entity.AnswerEvaluation;
import com.interview.backend.entity.InterviewSession;
import com.interview.backend.repository.AnswerEvaluationRepository;
import com.interview.backend.repository.InterviewSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

private final InterviewSessionRepository sessionRepository;
private final AnswerEvaluationRepository evaluationRepository;

public ResultService(InterviewSessionRepository sessionRepository,
    AnswerEvaluationRepository evaluationRepository) {

    this.sessionRepository = sessionRepository;
    this.evaluationRepository = evaluationRepository;
    }

@Transactional(readOnly = true)
public InterviewResultResponseDTO getInterviewResult(Integer sessionId) {
InterviewSession session = sessionRepository.findById(sessionId)
.orElseThrow(() -> new RuntimeException("Interview session not found: " + sessionId));

 List<AnswerEvaluation> evaluations = evaluationRepository.findBySessionId(sessionId);
List<EvaluatedQuestionDTO> evaluatedQuestionDTOs = evaluations.stream()
    .map(e -> new EvaluatedQuestionDTO(
    e.getQuestion().getQuestionText(),
    e.getUserAnswer(),
    e.getQuestion().getIdealAnswer(),
    e.getSimilarityScore(),
    e.getRemark()
        ))
    .collect(Collectors.toList());

BigDecimal totalScore = session.getTotalScore() != null ? session.getTotalScore() : BigDecimal.ZERO;
String overallRemark = determineOverallRemark(totalScore);

return new InterviewResultResponseDTO(session.getId(), totalScore, overallRemark, evaluatedQuestionDTOs);
}

@Transactional(readOnly = true)
public List<InterviewHistoryResponseDTO> getUserHistory(Integer userId) {
List<InterviewSession> sessions = sessionRepository.findByUserIdOrderByCreatedAtDesc(userId);

return sessions.stream()
    .map(s -> new InterviewHistoryResponseDTO(
            s.getId(),
            s.getRole().getName(),
            s.getTotalScore(),
            s.getCreatedAt()
    ))
    .collect(Collectors.toList());
    }

private String determineOverallRemark(BigDecimal score) {
    double val = score.doubleValue();
    if (val >= 85.0) return "Outstanding Performance - Hire Ready!";
    if (val >= 70.0) return "Good Performance - Solid Technical Foundation.";
    if (val >= 50.0) return "Average Performance - Needs Further Preparation.";
    return "Below Expectations - Requires Significant Study.";
    }
}
