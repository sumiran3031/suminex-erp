package com.suminex.erp.dto;

import java.util.Map;

public class ResultAnalyticsResponse {

    private String subjectName;
    private int totalStudents;
    private double classAverage;
    private double passPercentage;
    private Map<String, Long> gradeDistribution;

    public ResultAnalyticsResponse(String subjectName, int totalStudents, double classAverage,
                                   double passPercentage, Map<String, Long> gradeDistribution) {
        this.subjectName = subjectName;
        this.totalStudents = totalStudents;
        this.classAverage = classAverage;
        this.passPercentage = passPercentage;
        this.gradeDistribution = gradeDistribution;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public double getClassAverage() {
        return classAverage;
    }

    public double getPassPercentage() {
        return passPercentage;
    }

    public Map<String, Long> getGradeDistribution() {
        return gradeDistribution;
    }
}