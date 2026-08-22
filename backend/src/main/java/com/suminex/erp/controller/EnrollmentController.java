package com.suminex.erp.controller;

import com.suminex.erp.dto.CreateEnrollmentRequest;
import com.suminex.erp.dto.EnrollmentResponse;
import com.suminex.erp.dto.PromoteEnrollmentRequest;
import com.suminex.erp.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<EnrollmentResponse> createEnrollment(@Valid @RequestBody CreateEnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/promote")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<EnrollmentResponse> promoteEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody PromoteEnrollmentRequest request
    ) {
        EnrollmentResponse response = enrollmentService.promoteEnrollment(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> getStudentHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getStudentHistory(studentId));
    }

    @GetMapping("/division/{divisionId}/active")
    public ResponseEntity<List<EnrollmentResponse>> getActiveByDivision(@PathVariable Long divisionId) {
        return ResponseEntity.ok(enrollmentService.getActiveByDivision(divisionId));
    }
}