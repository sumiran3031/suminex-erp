package com.suminex.erp.controller;

import com.suminex.erp.dto.AttendanceResponse;
import com.suminex.erp.dto.MarkAttendanceRequest;
import com.suminex.erp.dto.SessionRosterResponse;
import com.suminex.erp.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/roster/{teachingSessionId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<List<SessionRosterResponse>> getRoster(@PathVariable Long teachingSessionId) {
        return ResponseEntity.ok(attendanceService.getRosterForSession(teachingSessionId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<List<AttendanceResponse>> markAttendance(@Valid @RequestBody MarkAttendanceRequest request) {
        List<AttendanceResponse> response = attendanceService.markAttendance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/session/{teachingSessionId}")
    public ResponseEntity<List<AttendanceResponse>> getBySession(@PathVariable Long teachingSessionId) {
        return ResponseEntity.ok(attendanceService.getBySession(teachingSessionId));
    }
}