package com.suminex.erp.controller;

import com.suminex.erp.dto.SubjectRequest;
import com.suminex.erp.dto.SubjectResponse;
import com.suminex.erp.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest request) {
        SubjectResponse response = subjectService.createSubject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getById(id));
    }

    @GetMapping("/by-semester/{semesterId}")
    public ResponseEntity<List<SubjectResponse>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(subjectService.getBySemester(semesterId));
    }

    @GetMapping("/by-course/{courseProgramId}")
    public ResponseEntity<List<SubjectResponse>> getByCourseProgram(@PathVariable Long courseProgramId) {
        return ResponseEntity.ok(subjectService.getByCourseProgram(courseProgramId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}