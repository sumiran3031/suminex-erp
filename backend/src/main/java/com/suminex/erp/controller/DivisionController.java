package com.suminex.erp.controller;

import com.suminex.erp.dto.DivisionRequest;
import com.suminex.erp.dto.DivisionResponse;
import com.suminex.erp.service.DivisionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divisions")
public class DivisionController {

    private final DivisionService divisionService;

    public DivisionController(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<DivisionResponse> createDivision(@Valid @RequestBody DivisionRequest request) {
        DivisionResponse response = divisionService.createDivision(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-semester/{semesterId}")
    public ResponseEntity<List<DivisionResponse>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(divisionService.getBySemester(semesterId));
    }
}