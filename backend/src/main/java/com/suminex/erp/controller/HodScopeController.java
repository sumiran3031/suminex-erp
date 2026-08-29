package com.suminex.erp.controller;

import com.suminex.erp.dto.SubjectResponse;
import com.suminex.erp.dto.TeacherResponse;
import com.suminex.erp.security.CustomUserDetails;
import com.suminex.erp.service.HodScopeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('HOD')")
public class HodScopeController {

    private final HodScopeService hodScopeService;

    public HodScopeController(HodScopeService hodScopeService) {
        this.hodScopeService = hodScopeService;
    }

    @GetMapping("/api/teachers/my-department")
    public ResponseEntity<List<TeacherResponse>> getMyDepartmentTeachers(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(hodScopeService.getMyDepartmentTeachers(userDetails.getUserId()));
    }

    @GetMapping("/api/subjects/my-department")
    public ResponseEntity<List<SubjectResponse>> getMyDepartmentSubjects(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(hodScopeService.getMyDepartmentSubjects(userDetails.getUserId()));
    }

    @GetMapping("/api/students/my-department")
    public ResponseEntity<List<HodScopeService.StudentSummary>> getMyDepartmentStudents(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(hodScopeService.getMyDepartmentStudents(userDetails.getUserId()));
    }
}