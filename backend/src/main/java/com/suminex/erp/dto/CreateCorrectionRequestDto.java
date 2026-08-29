package com.suminex.erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCorrectionRequestDto {

    @NotNull(message = "Marks entry id is required")
    private Long marksEntryId;

    @NotBlank(message = "Reason is required")
    private String reason;

    @Min(value = 0, message = "Marks cannot be negative")
    private int proposedInternalMarks;

    @Min(value = 0, message = "Marks cannot be negative")
    private int proposedExternalMarks;

    @Min(value = 0, message = "Marks cannot be negative")
    private int proposedPracticalMarks;

    public Long getMarksEntryId() {
        return marksEntryId;
    }

    public void setMarksEntryId(Long marksEntryId) {
        this.marksEntryId = marksEntryId;
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
}