package com.suminex.erp.controller;

import com.suminex.erp.dto.SemesterRequest;
import com.suminex.erp.dto.SemesterResponse;
import com.suminex.erp.service.SemesterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<SemesterResponse> createSemester(@Valid @RequestBody SemesterRequest request) {
        SemesterResponse response = semesterService.createSemester(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-academic-year/{academicYearId}")
    public ResponseEntity<List<SemesterResponse>> getByAcademicYear(@PathVariable Long academicYearId) {
        return ResponseEntity.ok(semesterService.getByAcademicYear(academicYearId));
    }
}