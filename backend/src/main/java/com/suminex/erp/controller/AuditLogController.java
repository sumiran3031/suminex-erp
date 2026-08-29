package com.suminex.erp.controller;

import com.suminex.erp.dto.AuditLogResponse;
import com.suminex.erp.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    // GET only — deliberately no POST/PUT/PATCH/DELETE exposed here.
    // Audit logs are created internally via AuditLogService.log(...), never through this API.
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<AuditLogResponse>> getLogs(
            @RequestParam(required = false) String entityType
    ) {
        if (entityType != null && !entityType.isBlank()) {
            return ResponseEntity.ok(auditLogService.getLogsByEntityType(entityType));
        }
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }
}