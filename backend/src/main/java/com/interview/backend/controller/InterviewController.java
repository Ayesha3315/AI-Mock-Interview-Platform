package com.interview.backend.controller;

import com.interview.backend.dto.AnswerSubmissionRequestDTO;
import com.interview.backend.dto.AnswerSubmissionResponseDTO;
import com.interview.backend.dto.InterviewStartRequestDTO;
import com.interview.backend.dto.InterviewStartResponseDTO;
import com.interview.backend.service.EvaluationService;
import com.interview.backend.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;
    private final EvaluationService evaluationService;

    public InterviewController(InterviewService interviewService, EvaluationService evaluationService) {
        this.interviewService = interviewService;
        this.evaluationService = evaluationService;
    }

    @PostMapping("/start")
    public ResponseEntity<InterviewStartResponseDTO> startInterview(
            @RequestParam Integer userId,
            @RequestBody InterviewStartRequestDTO request) {
        InterviewStartResponseDTO response = interviewService.startInterview(userId, request.getRoleId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/answer")
    public ResponseEntity<AnswerSubmissionResponseDTO> submitAnswer(
            @RequestBody AnswerSubmissionRequestDTO request) {
        AnswerSubmissionResponseDTO response = evaluationService.evaluateAndSaveAnswer(request);
        return ResponseEntity.ok(response);
    }
}
