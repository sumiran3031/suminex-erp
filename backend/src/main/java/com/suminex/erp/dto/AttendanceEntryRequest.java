package com.suminex.erp.dto;

import com.suminex.erp.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public class AttendanceEntryRequest {

    @NotNull(message = "Student id is required")
    private Long studentId;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}