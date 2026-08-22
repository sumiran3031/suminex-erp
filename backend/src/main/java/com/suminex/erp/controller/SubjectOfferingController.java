package com.suminex.erp.controller;

import com.suminex.erp.dto.SubjectOfferingRequest;
import com.suminex.erp.dto.SubjectOfferingResponse;
import com.suminex.erp.service.SubjectOfferingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject-offerings")
public class SubjectOfferingController {

    private final SubjectOfferingService subjectOfferingService;

    public SubjectOfferingController(SubjectOfferingService subjectOfferingService) {
        this.subjectOfferingService = subjectOfferingService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<SubjectOfferingResponse> createSubjectOffering(
            @Valid @RequestBody SubjectOfferingRequest request
    ) {
        SubjectOfferingResponse response = subjectOfferingService.createSubjectOffering(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<List<SubjectOfferingResponse>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(subjectOfferingService.getByTeacher(teacherId));
    }

    @GetMapping("/by-division/{divisionId}")
    public ResponseEntity<List<SubjectOfferingResponse>> getByDivision(@PathVariable Long divisionId) {
        return ResponseEntity.ok(subjectOfferingService.getByDivision(divisionId));
    }

    @GetMapping("/by-semester/{semesterId}")
    public ResponseEntity<List<SubjectOfferingResponse>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(subjectOfferingService.getBySemester(semesterId));
    }
}