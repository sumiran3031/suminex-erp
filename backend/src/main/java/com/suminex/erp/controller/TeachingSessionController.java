package com.suminex.erp.controller;

import com.suminex.erp.dto.CreateTeachingSessionRequest;
import com.suminex.erp.dto.TeachingSessionResponse;
import com.suminex.erp.service.TeachingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teaching-sessions")
public class TeachingSessionController {

    private final TeachingSessionService teachingSessionService;

    public TeachingSessionController(TeachingSessionService teachingSessionService) {
        this.teachingSessionService = teachingSessionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<TeachingSessionResponse> createSession(
            @Valid @RequestBody CreateTeachingSessionRequest request
    ) {
        TeachingSessionResponse response = teachingSessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<List<TeachingSessionResponse>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(teachingSessionService.getByTeacher(teacherId));
    }

    @GetMapping("/by-offering/{subjectOfferingId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<List<TeachingSessionResponse>> getBySubjectOffering(@PathVariable Long subjectOfferingId) {
        return ResponseEntity.ok(teachingSessionService.getBySubjectOffering(subjectOfferingId));
    }
}