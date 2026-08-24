package com.suminex.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grade_bands")
public class GradeBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grading_scheme_id", nullable = false)
    private GradingScheme gradingScheme;

    @Column(name = "min_marks", nullable = false)
    private int minMarks;

    @Column(name = "max_marks", nullable = false)
    private int maxMarks;

    @Column(nullable = false)
    private String grade;

    @Column(name = "grade_point", nullable = false)
    private double gradePoint;

    @Column(name = "is_pass", nullable = false)
    private boolean isPass;

    public GradeBand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GradingScheme getGradingScheme() {
        return gradingScheme;
    }

    public void setGradingScheme(GradingScheme gradingScheme) {
        this.gradingScheme = gradingScheme;
    }

    public int getMinMarks() {
        return minMarks;
    }

    public void setMinMarks(int minMarks) {
        this.minMarks = minMarks;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }
}