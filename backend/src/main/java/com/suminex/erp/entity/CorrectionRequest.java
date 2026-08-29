package com.suminex.erp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "correction_requests")
public class CorrectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marks_entry_id", nullable = false)
    private MarksEntry marksEntry;

    @Column(name = "requested_by_email", nullable = false)
    private String requestedByEmail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "proposed_internal_marks", nullable = false)
    private int proposedInternalMarks;

    @Column(name = "proposed_external_marks", nullable = false)
    private int proposedExternalMarks;

    @Column(name = "proposed_practical_marks", nullable = false)
    private int proposedPracticalMarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CorrectionRequestStatus status;

    @Column(name = "reviewed_by_email")
    private String reviewedByEmail;

    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public CorrectionRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MarksEntry getMarksEntry() {
        return marksEntry;
    }

    public void setMarksEntry(MarksEntry marksEntry) {
        this.marksEntry = marksEntry;
    }

    public String getRequestedByEmail() {
        return requestedByEmail;
    }

    public void setRequestedByEmail(String requestedByEmail) {
        this.requestedByEmail = requestedByEmail;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getProposedInternalMarks() {
        return proposedInternalMarks;
    }

    public void setProposedInternalMarks(int proposedInternalMarks) {
        this.proposedInternalMarks = proposedInternalMarks;
    }

    public int getProposedExternalMarks() {
        return proposedExternalMarks;
    }

    public void setProposedExternalMarks(int proposedExternalMarks) {
        this.proposedExternalMarks = proposedExternalMarks;
    }

    public int getProposedPracticalMarks() {
        return proposedPracticalMarks;
    }

    public void setProposedPracticalMarks(int proposedPracticalMarks) {
        this.proposedPracticalMarks = proposedPracticalMarks;
    }

    public CorrectionRequestStatus getStatus() {
        return status;
    }

    public void setStatus(CorrectionRequestStatus status) {
        this.status = status;
    }

    public String getReviewedByEmail() {
        return reviewedByEmail;
    }

    public void setReviewedByEmail(String reviewedByEmail) {
        this.reviewedByEmail = reviewedByEmail;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}