package com.suminex.erp.controller;

import com.suminex.erp.dto.CourseProgramRequest;
import com.suminex.erp.dto.CourseProgramResponse;
import com.suminex.erp.service.CourseProgramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-programs")
public class CourseProgramController {

    private final CourseProgramService courseProgramService;

    public CourseProgramController(CourseProgramService courseProgramService) {
        this.courseProgramService = courseProgramService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<CourseProgramResponse> createCourseProgram(@Valid @RequestBody CourseProgramRequest request) {
        CourseProgramResponse response = courseProgramService.createCourseProgram(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CourseProgramResponse>> getAllCourseProgams() {
        return ResponseEntity.ok(courseProgramService.getAllCoursePrograms());
    }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<CourseProgramResponse>> getByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(courseProgramService.getByDepartment(departmentId));
    }
}