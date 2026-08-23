package com.suminex.erp.dto;

import com.suminex.erp.entity.SessionStatus;

import java.time.LocalDate;

public class TeachingSessionResponse {

    private Long id;
    private Long timetableId;
    private String subjectName;
    private String teacherName;
    private String divisionName;
    private LocalDate sessionDate;
    private SessionStatus status;

    public TeachingSessionResponse(Long id, Long timetableId, String subjectName, String teacherName,
                                   String divisionName, LocalDate sessionDate, SessionStatus status) {
        this.id = id;
        this.timetableId = timetableId;
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.divisionName = divisionName;
        this.sessionDate = sessionDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getTimetableId() {
        return timetableId;
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

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public SessionStatus getStatus() {
        return status;
    }
}