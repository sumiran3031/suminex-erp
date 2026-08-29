package com.suminex.erp.controller;

import com.suminex.erp.dto.AssignmentResponse;
import com.suminex.erp.dto.AssignmentSubmissionResponse;
import com.suminex.erp.security.CustomUserDetails;
import com.suminex.erp.service.AssignmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<AssignmentResponse> createAssignment(
            @RequestParam Long subjectOfferingId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate,
            @RequestParam(required = false) MultipartFile file
    ) {
        AssignmentResponse response = assignmentService.createAssignment(
                subjectOfferingId, title, description, dueDate, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/{id}/submit", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AssignmentSubmissionResponse> submitAssignment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam MultipartFile file
    ) {
        // studentId is never taken from the request — always derived from the
        // authenticated user's own token, so a student can only submit as themselves.
        AssignmentSubmissionResponse response =
                assignmentService.submitAssignment(id, userDetails.getUserId(), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-offering/{subjectOfferingId}")
    public ResponseEntity<List<AssignmentResponse>> getBySubjectOffering(@PathVariable Long subjectOfferingId) {
        return ResponseEntity.ok(assignmentService.getBySubjectOffering(subjectOfferingId));
    }

    @GetMapping("/{id}/submissions")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<List<AssignmentSubmissionResponse>> getSubmissions(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getSubmissions(id));
    }
}