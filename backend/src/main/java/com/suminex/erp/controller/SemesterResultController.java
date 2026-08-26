package com.suminex.erp.controller;

import com.suminex.erp.dto.SemesterResultResponse;
import com.suminex.erp.service.SemesterResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/semester-results")
public class SemesterResultController {

    private final SemesterResultService semesterResultService;

    public SemesterResultController(SemesterResultService semesterResultService) {
        this.semesterResultService = semesterResultService;
    }

    @PostMapping("/calculate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<SemesterResultResponse> calculate(
            @RequestParam Long studentId,
            @RequestParam Long semesterId
    ) {
        SemesterResultResponse response = semesterResultService.calculateSgpa(studentId, semesterId);
        return ResponseEntity.ok(response);
    }
}