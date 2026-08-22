package com.suminex.erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SemesterRequest {

    @NotNull(message = "Academic year id is required")
    private Long academicYearId;

    @Min(value = 1, message = "Semester number must be at least 1")
    private int semesterNumber;

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }
}