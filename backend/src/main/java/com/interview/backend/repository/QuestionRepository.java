package com.interview.backend.repository;

import com.interview.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByRoleIdOrderByIdAsc(Integer roleId);
}