package com.suminex.erp.dto;

public class AttendanceAnalyticsResponse {

    private Long studentId;
    private String studentName;
    private long totalSessions;
    private long presentCount;
    private double attendancePercentage;

    public AttendanceAnalyticsResponse(Long studentId, String studentName, long totalSessions,
                                       long presentCount, double attendancePercentage) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.totalSessions = totalSessions;
        this.presentCount = presentCount;
        this.attendancePercentage = attendancePercentage;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public long getPresentCount() {
        return presentCount;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }
}