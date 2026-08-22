package com.suminex.erp.dto;

import com.suminex.erp.entity.DayOfWeek;

import java.time.LocalTime;

public class TimetableResponse {

    private Long id;
    private Long subjectOfferingId;
    private String subjectName;
    private String teacherName;
    private String divisionName;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomName;

    public TimetableResponse(Long id, Long subjectOfferingId, String subjectName, String teacherName,
                             String divisionName, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime,
                             String roomName) {
        this.id = id;
        this.subjectOfferingId = subjectOfferingId;
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.divisionName = divisionName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomName = roomName;
    }

    public Long getId() {
        return id;
    }

    public Long getSubjectOfferingId() {
        return subjectOfferingId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getRoomName() {
        return roomName;
    }
}