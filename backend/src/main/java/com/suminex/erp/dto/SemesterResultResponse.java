package com.suminex.erp.dto;

import java.time.LocalDateTime;

public class SemesterResultResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private int semesterNumber;
    private double sgpa;
    private String status;
    private LocalDateTime calculatedAt;

    public SemesterResultResponse(Long id, Long studentId, String studentName, int semesterNumber,
                                  double sgpa, String status, LocalDateTime calculatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.semesterNumber = semesterNumber;
        this.sgpa = sgpa;
        this.status = status;
        this.calculatedAt = calculatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public double getSgpa() {
        return sgpa;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
}