package com.suminex.erp.dto;

import java.time.LocalTime;

public class TimeSlotResponse {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlotResponse(Long id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}