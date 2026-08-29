package com.suminex.erp.controller;

import com.suminex.erp.dto.CorrectionRequestResponse;
import com.suminex.erp.dto.CreateCorrectionRequestDto;
import com.suminex.erp.service.CorrectionRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/correction-requests")
public class CorrectionRequestController {

    private final CorrectionRequestService correctionRequestService;

    public CorrectionRequestController(CorrectionRequestService correctionRequestService) {
        this.correctionRequestService = correctionRequestService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER')")
    public ResponseEntity<CorrectionRequestResponse> createRequest(@Valid @RequestBody CreateCorrectionRequestDto dto) {
        CorrectionRequestResponse response = correctionRequestService.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<CorrectionRequestResponse> approve(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String notes = body != null ? body.getOrDefault("notes", "") : "";
        return ResponseEntity.ok(correctionRequestService.approveRequest(id, notes));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<CorrectionRequestResponse> reject(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String notes = body != null ? body.getOrDefault("notes", "") : "";
        return ResponseEntity.ok(correctionRequestService.rejectRequest(id, notes));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD')")
    public ResponseEntity<List<CorrectionRequestResponse>> getPending() {
        return ResponseEntity.ok(correctionRequestService.getPending());
    }
}