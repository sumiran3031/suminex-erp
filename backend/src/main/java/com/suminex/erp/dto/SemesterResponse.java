package com.suminex.erp.dto;

public class SemesterResponse {

    private Long id;
    private Long academicYearId;
    private String academicYearLabel;
    private int semesterNumber;

    public SemesterResponse(Long id, Long academicYearId, String academicYearLabel, int semesterNumber) {
        this.id = id;
        this.academicYearId = academicYearId;
        this.academicYearLabel = academicYearLabel;
        this.semesterNumber = semesterNumber;
    }

    public Long getId() {
        return id;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public String getAcademicYearLabel() {
        return academicYearLabel;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }
}