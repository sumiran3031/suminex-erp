package com.suminex.erp.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateTeachingSessionRequest {

    @NotNull(message = "Timetable id is required")
    private Long timetableId;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    public Long getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(Long timetableId) {
        this.timetableId = timetableId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }
}