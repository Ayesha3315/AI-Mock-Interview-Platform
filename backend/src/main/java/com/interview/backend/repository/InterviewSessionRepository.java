package com.interview.backend.repository;

import com.interview.backend.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Integer> {
    List<InterviewSession> findByUserIdOrderByCreatedAtDesc(Integer userId);
}