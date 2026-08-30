package com.suminex.erp.controller;

import com.suminex.erp.dto.CreateTeacherRequest;
import com.suminex.erp.dto.TeacherResponse;
import com.suminex.erp.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody CreateTeacherRequest request) {
        TeacherResponse response = teacherService.createTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }
}