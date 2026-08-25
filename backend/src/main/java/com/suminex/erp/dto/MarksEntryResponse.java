package com.suminex.erp.dto;

import com.suminex.erp.entity.MarksEntryStatus;

public class MarksEntryResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String subjectName;
    private int internalMarks;
    private int externalMarks;
    private int practicalMarks;
    private int total;
    private String grade;
    private double gradePoint;
    private boolean pass;
    private MarksEntryStatus status;

    public MarksEntryResponse(Long id, Long studentId, String studentName, String subjectName,
                              int internalMarks, int externalMarks, int practicalMarks, int total,
                              String grade, double gradePoint, boolean pass, MarksEntryStatus status) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.internalMarks = internalMarks;
        this.externalMarks = externalMarks;
        this.practicalMarks = practicalMarks;
        this.total = total;
        this.grade = grade;
        this.gradePoint = gradePoint;
        this.pass = pass;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getInternalMarks() {
        return internalMarks;
    }

    public int getExternalMarks() {
        return externalMarks;
    }

    public int getPracticalMarks() {
        return practicalMarks;
    }

    public int getTotal() {
        return total;
    }

    public String getGrade() {
        return grade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public boolean isPass() {
        return pass;
    }

    public MarksEntryStatus getStatus() {
        return status;
    }
}