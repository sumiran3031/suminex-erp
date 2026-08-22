package com.suminex.erp.dto;

public class DivisionResponse {

    private Long id;
    private Long semesterId;
    private int semesterNumber;
    private String divisionName;

    public DivisionResponse(Long id, Long semesterId, int semesterNumber, String divisionName) {
        this.id = id;
        this.semesterId = semesterId;
        this.semesterNumber = semesterNumber;
        this.divisionName = divisionName;
    }

    public Long getId() {
        return id;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public String getDivisionName() {
        return divisionName;
    }
}