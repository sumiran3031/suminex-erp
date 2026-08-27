package com.suminex.erp.controller;

import com.suminex.erp.dto.ResultVerificationResponse;
import com.suminex.erp.entity.SemesterResult;
import com.suminex.erp.repository.SemesterResultRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/results")
public class ResultVerificationController {

    private final SemesterResultRepository semesterResultRepository;

    public ResultVerificationController(SemesterResultRepository semesterResultRepository) {
        this.semesterResultRepository = semesterResultRepository;
    }

    @GetMapping("/verify")
    public ResponseEntity<ResultVerificationResponse> verify(
            @RequestParam Long studentId,
            @RequestParam Long semesterId
    ) {
        return semesterResultRepository.findByStudentIdAndSemesterId(studentId, semesterId)
                .map(result -> ResponseEntity.ok(new ResultVerificationResponse(
                        true,
                        result.getStudent().getFirstName() + " " + result.getStudent().getLastName(),
                        result.getSemester().getSemesterNumber(),
                        result.getSgpa(),
                        "This result is valid and verified."
                )))
                .orElseGet(() -> ResponseEntity.ok(new ResultVerificationResponse(
                        false, null, 0, 0,
                        "No matching result found. This document could not be verified."
                )));
    }
}