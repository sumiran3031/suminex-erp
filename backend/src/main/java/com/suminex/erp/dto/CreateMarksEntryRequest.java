package com.suminex.erp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateMarksEntryRequest {

    @NotNull(message = "Subject offering id is required")
    private Long subjectOfferingId;

    @NotNull(message = "Student id is required")
    private Long studentId;

    @Min(value = 0, message = "Internal marks cannot be negative")
    private int internalMarks;

    @Min(value = 0, message = "External marks cannot be negative")
    private int externalMarks;

    @Min(value = 0, message = "Practical marks cannot be negative")
    private int practicalMarks;

    public Long getSubjectOfferingId() {
        return subjectOfferingId;
    }

    public void setSubjectOfferingId(Long subjectOfferingId) {
        this.subjectOfferingId = subjectOfferingId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public int getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(int internalMarks) {
        this.internalMarks = internalMarks;
    }

    public int getExternalMarks() {
        return externalMarks;
    }

    public void setExternalMarks(int externalMarks) {
        this.externalMarks = externalMarks;
    }

    public int getPracticalMarks() {
        return practicalMarks;
    }

    public void setPracticalMarks(int practicalMarks) {
        this.practicalMarks = practicalMarks;
    }
}