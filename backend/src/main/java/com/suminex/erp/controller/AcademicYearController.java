package com.suminex.erp.controller;

import com.suminex.erp.dto.AcademicYearRequest;
import com.suminex.erp.dto.AcademicYearResponse;
import com.suminex.erp.service.AcademicYearService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-years")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<AcademicYearResponse> createAcademicYear(@Valid @RequestBody AcademicYearRequest request) {
        AcademicYearResponse response = academicYearService.createAcademicYear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-course/{courseProgramId}")
    public ResponseEntity<List<AcademicYearResponse>> getByCourseProgram(@PathVariable Long courseProgramId) {
        return ResponseEntity.ok(academicYearService.getByCourseProgram(courseProgramId));
    }
}