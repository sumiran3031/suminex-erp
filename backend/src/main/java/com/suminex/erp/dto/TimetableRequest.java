package com.suminex.erp.dto;

import com.suminex.erp.entity.DayOfWeek;
import jakarta.validation.constraints.NotNull;

public class TimetableRequest {

    @NotNull(message = "Subject offering id is required")
    private Long subjectOfferingId;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Time slot id is required")
    private Long timeSlotId;

    @NotNull(message = "Room id is required")
    private Long roomId;

    public Long getSubjectOfferingId() {
        return subjectOfferingId;
    }

    public void setSubjectOfferingId(Long subjectOfferingId) {
        this.subjectOfferingId = subjectOfferingId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}