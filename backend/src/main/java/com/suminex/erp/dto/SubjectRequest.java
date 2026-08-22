package com.suminex.erp.dto;

import com.suminex.erp.entity.SubjectType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubjectRequest {

    @NotBlank(message = "Subject code is required")
    private String code;

    @NotBlank(message = "Subject name is required")
    private String name;

    @Min(value = 1, message = "Credits must be at least 1")
    private int credits;

    @NotNull(message = "Subject type is required")
    private SubjectType subjectType;

    @NotNull(message = "Course program id is required")
    private Long courseProgramId;

    @NotNull(message = "Semester id is required")
    private Long semesterId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public Long getCourseProgramId() {
        return courseProgramId;
    }

    public void setCourseProgramId(Long courseProgramId) {
        this.courseProgramId = courseProgramId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }
}