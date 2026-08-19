package com.interview.backend.controller;


import com.interview.backend.dto.InterviewHistoryResponseDTO;
import com.interview.backend.dto.InterviewResultResponseDTO;
import com.interview.backend.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/result/{sessionId}")
    public ResponseEntity<InterviewResultResponseDTO> getInterviewResult(@PathVariable Integer sessionId) {
        InterviewResultResponseDTO result = resultService.getInterviewResult(sessionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<InterviewHistoryResponseDTO>> getUserHistory(@PathVariable Integer userId) {
        List<InterviewHistoryResponseDTO> history = resultService.getUserHistory(userId);
        return ResponseEntity.ok(history);
    }
}

