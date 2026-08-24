package com.suminex.erp.controller;

import com.suminex.erp.dto.CreateGradingSchemeRequest;
import com.suminex.erp.dto.GradingSchemeResponse;
import com.suminex.erp.service.GradingSchemeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grading-schemes")
public class GradingSchemeController {

    private final GradingSchemeService gradingSchemeService;

    public GradingSchemeController(GradingSchemeService gradingSchemeService) {
        this.gradingSchemeService = gradingSchemeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<GradingSchemeResponse> createScheme(@Valid @RequestBody CreateGradingSchemeRequest request) {
        GradingSchemeResponse response = gradingSchemeService.createScheme(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<GradingSchemeResponse> activateScheme(@PathVariable Long id) {
        return ResponseEntity.ok(gradingSchemeService.activateScheme(id));
    }
}