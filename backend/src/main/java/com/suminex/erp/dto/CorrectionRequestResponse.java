package com.suminex.erp.dto;

import com.suminex.erp.entity.CorrectionRequestStatus;

import java.time.LocalDateTime;

public class CorrectionRequestResponse {

    private Long id;
    private Long marksEntryId;
    private String studentName;
    private String subjectName;
    private String requestedByEmail;
    private String reason;
    private int currentTotal;
    private int proposedInternalMarks;
    private int proposedExternalMarks;
    private int proposedPracticalMarks;
    private CorrectionRequestStatus status;
    private String reviewedByEmail;
    private String reviewNotes;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    public CorrectionRequestResponse(Long id, Long marksEntryId, String studentName, String subjectName,
                                     String requestedByEmail, String reason, int currentTotal,
                                     int proposedInternalMarks, int proposedExternalMarks,
                                     int proposedPracticalMarks, CorrectionRequestStatus status,
                                     String reviewedByEmail, String reviewNotes,
                                     LocalDateTime createdAt, LocalDateTime reviewedAt) {
        this.id = id;
        this.marksEntryId = marksEntryId;
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.requestedByEmail = requestedByEmail;
        this.reason = reason;
        this.currentTotal = currentTotal;
        this.proposedInternalMarks = proposedInternalMarks;
        this.proposedExternalMarks = proposedExternalMarks;
        this.proposedPracticalMarks = proposedPracticalMarks;
        this.status = status;
        this.reviewedByEmail = reviewedByEmail;
        this.reviewNotes = reviewNotes;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMarksEntryId() {
        return marksEntryId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getRequestedByEmail() {
        return requestedByEmail;
    }

    public String getReason() {
        return reason;
    }

    public int getCurrentTotal() {
        return currentTotal;
    }

    public int getProposedInternalMarks() {
        return proposedInternalMarks;
    }

    public int getProposedExternalMarks() {
        return proposedExternalMarks;
    }

    public int getProposedPracticalMarks() {
        return proposedPracticalMarks;
    }

    public CorrectionRequestStatus getStatus() {
        return status;
    }

    public String getReviewedByEmail() {
        return reviewedByEmail;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }
}