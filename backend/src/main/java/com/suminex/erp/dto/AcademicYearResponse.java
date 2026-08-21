package com.suminex.erp.dto;

public class AcademicYearResponse {

    private Long id;
    private Long courseProgramId;
    private String courseProgramName;
    private String yearLabel;
    private int yearNumber;

    public AcademicYearResponse(Long id, Long courseProgramId, String courseProgramName,
                                String yearLabel, int yearNumber) {
        this.id = id;
        this.courseProgramId = courseProgramId;
        this.courseProgramName = courseProgramName;
        this.yearLabel = yearLabel;
        this.yearNumber = yearNumber;
    }

    public Long getId() {
        return id;
    }

    public Long getCourseProgramId() {
        return courseProgramId;
    }

    public String getCourseProgramName() {
        return courseProgramName;
    }

    public String getYearLabel() {
        return yearLabel;
    }

    public int getYearNumber() {
        return yearNumber;
    }
}