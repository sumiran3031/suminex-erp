package com.suminex.erp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MarkAttendanceRequest {

    @NotNull(message = "Teaching session id is required")
    private Long teachingSessionId;

    @NotEmpty(message = "At least one attendance entry is required")
    @Valid
    private List<AttendanceEntryRequest> entries;

    public Long getTeachingSessionId() {
        return teachingSessionId;
    }

    public void setTeachingSessionId(Long teachingSessionId) {
        this.teachingSessionId = teachingSessionId;
    }

    public List<AttendanceEntryRequest> getEntries() {
        return entries;
    }

    public void setEntries(List<AttendanceEntryRequest> entries) {
        this.entries = entries;
    }
}