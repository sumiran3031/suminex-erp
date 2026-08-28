package com.suminex.erp.controller;

import com.suminex.erp.dto.AttendanceAnalyticsResponse;
import com.suminex.erp.dto.ResultAnalyticsResponse;
import com.suminex.erp.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/attendance/division/{divisionId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD', 'TEACHER')")
    public ResponseEntity<List<AttendanceAnalyticsResponse>> getDivisionAttendance(@PathVariable Long divisionId) {
        return ResponseEntity.ok(analyticsService.getDivisionAttendanceAnalytics(divisionId));
    }

    @GetMapping("/results/subject-offering/{subjectOfferingId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD', 'TEACHER')")
    public ResponseEntity<ResultAnalyticsResponse> getSubjectResults(@PathVariable Long subjectOfferingId) {
        return ResponseEntity.ok(analyticsService.getSubjectResultAnalytics(subjectOfferingId));
    }
}