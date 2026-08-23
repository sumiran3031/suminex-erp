package com.suminex.erp.dto;

public class SessionRosterResponse {

    private Long studentId;
    private String studentName;
    private String rollNumber;

    public SessionRosterResponse(Long studentId, String studentName, String rollNumber) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }
}