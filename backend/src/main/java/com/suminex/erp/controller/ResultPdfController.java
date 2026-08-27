package com.suminex.erp.controller;

import com.suminex.erp.service.ResultPdfService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/results")
public class ResultPdfController {

    private final ResultPdfService resultPdfService;

    public ResultPdfController(ResultPdfService resultPdfService) {
        this.resultPdfService = resultPdfService;
    }

    @GetMapping("/{studentId}/{semesterId}/pdf")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HOD', 'STUDENT')")
    public ResponseEntity<ByteArrayResource> downloadResultPdf(
            @PathVariable Long studentId,
            @PathVariable Long semesterId
    ) {
        byte[] pdfBytes = resultPdfService.generateResultPdf(studentId, semesterId);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result-" + studentId + "-" + semesterId + ".pdf")
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}