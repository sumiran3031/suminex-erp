package com.suminex.erp.controller;

import com.suminex.erp.dto.TimetableRequest;
import com.suminex.erp.dto.TimetableResponse;
import com.suminex.erp.security.CustomUserDetails;
import com.suminex.erp.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetables")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<TimetableResponse> createTimetableEntry(
            @Valid @RequestBody TimetableRequest request) {

        TimetableResponse response =
                timetableService.createTimetableEntry(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<List<TimetableResponse>> getByTeacher(
            @PathVariable Long teacherId) {

        return ResponseEntity.ok(
                timetableService.getByTeacher(teacherId)
        );
    }

    @GetMapping("/my-timetable")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<TimetableResponse>> getMyTimetable(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(
                timetableService.getMyTimetable(
                        userDetails.getUserId()
                )
        );
    }

    @GetMapping("/by-division/{divisionId}")
    public ResponseEntity<List<TimetableResponse>> getByDivision(
            @PathVariable Long divisionId) {

        return ResponseEntity.ok(
                timetableService.getByDivision(divisionId)
        );
    }
}