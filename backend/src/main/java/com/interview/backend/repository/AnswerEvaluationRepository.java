package com.interview.backend.repository;

import com.interview.backend.entity.AnswerEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerEvaluationRepository extends JpaRepository<AnswerEvaluation, Integer> {
    List<AnswerEvaluation> findBySessionId(Integer sessionId);
}
