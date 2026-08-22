package com.suminex.erp.controller;

import com.suminex.erp.dto.BatchRequest;
import com.suminex.erp.dto.BatchResponse;
import com.suminex.erp.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<BatchResponse> createBatch(@Valid @RequestBody BatchRequest request) {
        BatchResponse response = batchService.createBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-division/{divisionId}")
    public ResponseEntity<List<BatchResponse>> getByDivision(@PathVariable Long divisionId) {
        return ResponseEntity.ok(batchService.getByDivision(divisionId));
    }
}