package com.suminex.erp.dto;

import com.suminex.erp.entity.AttendanceStatus;

public class AttendanceResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private AttendanceStatus status;

    public AttendanceResponse(Long id, Long studentId, String studentName, AttendanceStatus status) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.status = status;
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

    public AttendanceStatus getStatus() {
        return status;
    }
}