package com.suminex.erp.dto;

public class CourseProgramResponse {

    private Long id;
    private Long departmentId;
    private String departmentName;
    private String name;
    private String code;
    private int durationYears;

    public CourseProgramResponse(Long id, Long departmentId, String departmentName, String name,
                                 String code, int durationYears) {
        this.id = id;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.name = name;
        this.code = code;
        this.durationYears = durationYears;
    }

    public Long getId() {
        return id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getDurationYears() {
        return durationYears;
    }
}