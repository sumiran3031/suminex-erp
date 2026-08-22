package com.suminex.erp.dto;

public class SubjectOfferingResponse {

    private Long id;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Long teacherId;
    private String teacherName;
    private Long academicYearId;
    private String academicYearLabel;
    private Long semesterId;
    private int semesterNumber;
    private Long divisionId;
    private String divisionName;
    private Long batchId;

    public SubjectOfferingResponse(Long id, Long subjectId, String subjectName, String subjectCode,
                                   Long teacherId, String teacherName, Long academicYearId, String academicYearLabel,
                                   Long semesterId, int semesterNumber, Long divisionId, String divisionName,
                                   Long batchId) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.academicYearId = academicYearId;
        this.academicYearLabel = academicYearLabel;
        this.semesterId = semesterId;
        this.semesterNumber = semesterNumber;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.batchId = batchId;
    }

    public Long getId() {
        return id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public String getAcademicYearLabel() {
        return academicYearLabel;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public Long getBatchId() {
        return batchId;
    }
}