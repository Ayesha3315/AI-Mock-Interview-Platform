package com.interview.backend.service;


import com.interview.backend.dto.InterviewStartResponseDTO;
import com.interview.backend.dto.QuestionResponseDTO;
import com.interview.backend.entity.AnswerEvaluation;
import com.interview.backend.entity.InterviewSession;
import com.interview.backend.entity.Question;
import com.interview.backend.entity.Role;
import com.interview.backend.entity.User;
import com.interview.backend.repository.AnswerEvaluationRepository;
import com.interview.backend.repository.InterviewSessionRepository;
import com.interview.backend.repository.QuestionRepository;
import com.interview.backend.repository.RoleRepository;
import com.interview.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AnswerEvaluationRepository evaluationRepository;

    public InterviewService(InterviewSessionRepository sessionRepository,QuestionRepository questionRepository,
        UserRepository userRepository,RoleRepository roleRepository,AnswerEvaluationRepository evaluationRepository) {

    this.sessionRepository = sessionRepository;
    this.questionRepository = questionRepository;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.evaluationRepository = evaluationRepository;
}

@Transactional
public InterviewStartResponseDTO startInterview(Integer userId, Integer roleId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

InterviewSession session = new InterviewSession();
session.setUser(user);
session.setRole(role);
session.setStatus("IN_PROGRESS");
InterviewSession savedSession = sessionRepository.save(session);

List<Question> questions = questionRepository.findByRoleIdOrderByIdAsc(roleId);

if (questions.isEmpty()) {
    throw new RuntimeException("No questions available for role id: " + roleId);
}

// Randomize using the interview session ID so the order stays fixed for this interview
java.util.Collections.shuffle(
        questions,
        new java.util.Random(savedSession.getId())
);

// Take the first random question
Question firstQuestion = questions.get(0);
QuestionResponseDTO questionDTO = new QuestionResponseDTO(firstQuestion.getId(), firstQuestion.getQuestionText());

return new InterviewStartResponseDTO(savedSession.getId(), questionDTO);
}

public QuestionResponseDTO getNextUnansweredQuestion(Integer sessionId) {
InterviewSession session = sessionRepository.findById(sessionId)
        .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));

List<Question> allQuestions =
    questionRepository.findByRoleIdOrderByIdAsc(session.getRole().getId());

// Use the same random order throughout this interview
java.util.Collections.shuffle(
        allQuestions,
        new java.util.Random(sessionId)
);
List<AnswerEvaluation> evaluated = evaluationRepository.findBySessionId(sessionId);

Set<Integer> answeredQuestionIds = evaluated.stream()
        .map(e -> e.getQuestion().getId())
        .collect(Collectors.toSet());

for (Question q : allQuestions) {
    if (!answeredQuestionIds.contains(q.getId())) {
        return new QuestionResponseDTO(q.getId(), q.getQuestionText());
    }
}

return null; // All answered
}
}

