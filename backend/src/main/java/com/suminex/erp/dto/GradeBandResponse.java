package com.suminex.erp.dto;

public class GradeBandResponse {

    private Long id;
    private int minMarks;
    private int maxMarks;
    private String grade;
    private double gradePoint;
    private boolean isPass;

    public GradeBandResponse(Long id, int minMarks, int maxMarks, String grade, double gradePoint, boolean isPass) {
        this.id = id;
        this.minMarks = minMarks;
        this.maxMarks = maxMarks;
        this.grade = grade;
        this.gradePoint = gradePoint;
        this.isPass = isPass;
    }

    public Long getId() {
        return id;
    }

    public int getMinMarks() {
        return minMarks;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public String getGrade() {
        return grade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public boolean isPass() {
        return isPass;
    }
}