package com.interview.backend.service;

import com.interview.backend.dto.AnswerSubmissionRequestDTO;
import com.interview.backend.dto.AnswerSubmissionResponseDTO;
import com.interview.backend.dto.QuestionResponseDTO;
import com.interview.backend.entity.AnswerEvaluation;
import com.interview.backend.entity.InterviewSession;
import com.interview.backend.entity.Question;
import com.interview.backend.repository.AnswerEvaluationRepository;
import com.interview.backend.repository.InterviewSessionRepository;
import com.interview.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationService {

private final AnswerEvaluationRepository evaluationRepository;
private final InterviewSessionRepository sessionRepository;
private final QuestionRepository questionRepository;
private final InterviewService interviewService;
private final RestTemplate restTemplate;

@Value("${flask.service.url:http://localhost:5000}")
private String flaskServiceUrl;

public EvaluationService(AnswerEvaluationRepository evaluationRepository,InterviewSessionRepository sessionRepository,
                        QuestionRepository questionRepository,InterviewService interviewService) {
this.evaluationRepository = evaluationRepository;
this.sessionRepository = sessionRepository;
this.questionRepository = questionRepository;
this.interviewService = interviewService;
this.restTemplate = new RestTemplate();
}

@Transactional
public AnswerSubmissionResponseDTO evaluateAndSaveAnswer(AnswerSubmissionRequestDTO request) {

InterviewSession session = sessionRepository.findById(request.getSessionId())
.orElseThrow(() -> new RuntimeException("Session not found: " + request.getSessionId()));


Question question = questionRepository.findById(request.getQuestionId())
.orElseThrow(() -> new RuntimeException("Question not found: " + request.getQuestionId()));

BigDecimal similarityScore;
String remark;

// Call Flask NLP service
try {
String evaluateUrl = flaskServiceUrl + "/evaluate";

HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);

Map<String, String> payload = new HashMap<>();
payload.put("user_answer", request.getUserAnswer());
payload.put("ideal_answer", question.getIdealAnswer());

HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);

// ParameterizedTypeReference eliminates raw type and unchecked cast warnings
ResponseEntity<Map<String, Object>> response = restTemplate.exchange(evaluateUrl,HttpMethod.POST,requestEntity,
        new ParameterizedTypeReference<Map<String, Object>>() {}
);

Map<String, Object> body = response.getBody();
if (response.getStatusCode().is2xxSuccessful() && body != null) {

    Object scoreObj = body.get("similarity_score");

    if (scoreObj instanceof Number) {
        similarityScore = BigDecimal
                .valueOf(((Number) scoreObj).doubleValue())
                .setScale(2, RoundingMode.HALF_UP);
    } else {
        throw new RuntimeException("Invalid similarity score returned from NLP Evaluation Service.");
    }

    Object remarkObj = body.get("remark");
    if (remarkObj != null) {
        remark = remarkObj.toString();
    } else {
        remark = determineRemark(similarityScore);
    }

    } else {
    throw new RuntimeException("Invalid response received from NLP Evaluation Service.");
        }

} catch (Exception e) {
throw new RuntimeException("Unable to connect to NLP Evaluation Service.", e);
    }

AnswerEvaluation evaluation = new AnswerEvaluation();
evaluation.setSession(session);
evaluation.setQuestion(question);
evaluation.setUserAnswer(request.getUserAnswer());
evaluation.setSimilarityScore(similarityScore);
evaluation.setRemark(remark);
evaluationRepository.save(evaluation);

// Count answers submitted for this interview
List<AnswerEvaluation> allEvals = evaluationRepository.findBySessionId(session.getId());

// Stop interview after 5 questions
boolean completed = allEvals.size() >= 5;
QuestionResponseDTO nextQ = null;

    if (!completed) {
        nextQ = interviewService.getNextUnansweredQuestion(session.getId());
    }

    if (completed) {
        session.setStatus("COMPLETED");

        if (!allEvals.isEmpty()) {
            // Lambdas prevent compiler null type safety warnings
            BigDecimal sum = allEvals.stream()
                    .map(eval -> eval.getSimilarityScore())
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

            BigDecimal avg = sum.divide(
                    BigDecimal.valueOf(allEvals.size()),
                    2,
                    RoundingMode.HALF_UP
            );

            session.setTotalScore(avg);
        } else {
            session.setTotalScore(BigDecimal.ZERO);
        }

        sessionRepository.save(session);
    }

return new AnswerSubmissionResponseDTO(similarityScore, remark, completed, nextQ);
}

private String determineRemark(BigDecimal score) {
    double val = score.doubleValue();
    if (val >= 80.0) return "Excellent understanding of the concept.";
    if (val >= 60.0) return "Good answer with minor gaps.";
    if (val >= 40.0) return "Satisfactory, but needs more technical depth.";
    return "Needs significant improvement.";
    }
}