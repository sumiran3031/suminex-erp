package com.suminex.erp.dto;

import jakarta.validation.constraints.NotNull;

public class PromoteEnrollmentRequest {

    @NotNull(message = "New academic year id is required")
    private Long academicYearId;

    @NotNull(message = "New semester id is required")
    private Long semesterId;

    @NotNull(message = "New division id is required")
    private Long divisionId;

    private Long batchId;

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Long divisionId) {
        this.divisionId = divisionId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }
}