package com.suminex.erp.dto;

import com.suminex.erp.entity.EnrollmentStatus;

import java.time.LocalDateTime;

public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String academicYearLabel;
    private int semesterNumber;
    private String divisionName;
    private Long batchId;
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime endedAt;

    public EnrollmentResponse(Long id, Long studentId, String studentName, String academicYearLabel,
                              int semesterNumber, String divisionName, Long batchId, EnrollmentStatus status,
                              LocalDateTime enrolledAt, LocalDateTime endedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.academicYearLabel = academicYearLabel;
        this.semesterNumber = semesterNumber;
        this.divisionName = divisionName;
        this.batchId = batchId;
        this.status = status;
        this.enrolledAt = enrolledAt;
        this.endedAt = endedAt;
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

    public String getAcademicYearLabel() {
        return academicYearLabel;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public Long getBatchId() {
        return batchId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }
}