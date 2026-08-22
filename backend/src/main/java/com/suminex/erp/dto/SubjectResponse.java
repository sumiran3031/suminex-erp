package com.suminex.erp.dto;

import com.suminex.erp.entity.SubjectType;

public class SubjectResponse {

    private Long id;
    private String code;
    private String name;
    private int credits;
    private SubjectType subjectType;
    private Long courseProgramId;
    private String courseProgramName;
    private Long semesterId;
    private int semesterNumber;

    public SubjectResponse(Long id, String code, String name, int credits, SubjectType subjectType,
                           Long courseProgramId, String courseProgramName, Long semesterId, int semesterNumber) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.subjectType = subjectType;
        this.courseProgramId = courseProgramId;
        this.courseProgramName = courseProgramName;
        this.semesterId = semesterId;
        this.semesterNumber = semesterNumber;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public Long getCourseProgramId() {
        return courseProgramId;
    }

    public String getCourseProgramName() {
        return courseProgramName;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }
}