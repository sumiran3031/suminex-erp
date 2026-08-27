package com.suminex.erp.dto;

import java.time.LocalDateTime;

public class AssignmentSubmissionResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String filePath;
    private LocalDateTime submittedAt;

    public AssignmentSubmissionResponse(Long id, Long studentId, String studentName, String filePath,
                                        LocalDateTime submittedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.filePath = filePath;
        this.submittedAt = submittedAt;
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

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}