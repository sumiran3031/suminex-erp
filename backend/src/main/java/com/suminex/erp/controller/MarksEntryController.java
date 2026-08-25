package com.suminex.erp.controller;

import com.suminex.erp.dto.CreateMarksEntryRequest;
import com.suminex.erp.dto.MarksEntryResponse;
import com.suminex.erp.service.MarksEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks-entries")
public class MarksEntryController {

    private final MarksEntryService marksEntryService;

    public MarksEntryController(MarksEntryService marksEntryService) {
        this.marksEntryService = marksEntryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<MarksEntryResponse> createMarksEntry(@Valid @RequestBody CreateMarksEntryRequest request) {
        MarksEntryResponse response = marksEntryService.createMarksEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-offering/{subjectOfferingId}")
    public ResponseEntity<List<MarksEntryResponse>> getBySubjectOffering(@PathVariable Long subjectOfferingId) {
        return ResponseEntity.ok(marksEntryService.getBySubjectOffering(subjectOfferingId));
    }

    @GetMapping("/by-student/{studentId}")
    public ResponseEntity<List<MarksEntryResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(marksEntryService.getByStudent(studentId));
    }
}