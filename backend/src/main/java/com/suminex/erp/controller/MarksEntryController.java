package com.suminex.erp.controller;

import com.suminex.erp.dto.CreateMarksEntryRequest;
import com.suminex.erp.dto.MarksEntryResponse;
import com.suminex.erp.dto.SessionRosterResponse;
import com.suminex.erp.dto.UpdateMarksStatusRequest;
import com.suminex.erp.security.CustomUserDetails;
import com.suminex.erp.service.MarksEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks-entries")
public class MarksEntryController {

    private final MarksEntryService marksEntryService;

    public MarksEntryController(
            MarksEntryService marksEntryService) {

        this.marksEntryService = marksEntryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<MarksEntryResponse> createMarksEntry(
            @Valid @RequestBody CreateMarksEntryRequest request) {

        MarksEntryResponse response =
                marksEntryService.createMarksEntry(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD', 'TEACHER')")
    public ResponseEntity<MarksEntryResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMarksStatusRequest request) {

        MarksEntryResponse response =
                marksEntryService.updateStatus(
                        id,
                        request.getStatus()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-offering/{subjectOfferingId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD', 'TEACHER')")
    public ResponseEntity<List<MarksEntryResponse>>
    getBySubjectOffering(
            @PathVariable Long subjectOfferingId) {

        return ResponseEntity.ok(
                marksEntryService.getBySubjectOffering(
                        subjectOfferingId
                )
        );
    }

    @GetMapping("/by-student/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD', 'TEACHER')")
    public ResponseEntity<List<MarksEntryResponse>>
    getByStudent(@PathVariable Long studentId) {

        return ResponseEntity.ok(
                marksEntryService.getByStudent(studentId)
        );
    }

    @GetMapping("/my-results")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<MarksEntryResponse>>
    getMyResults(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(
                marksEntryService.getMyResults(
                        userDetails.getUserId()
                )
        );
    }

    @GetMapping("/eligible-students/{subjectOfferingId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD', 'TEACHER')")
    public ResponseEntity<List<SessionRosterResponse>>
    getEligibleStudents(
            @PathVariable Long subjectOfferingId) {

        return ResponseEntity.ok(
                marksEntryService.getEligibleStudents(
                        subjectOfferingId
                )
        );
    }
}