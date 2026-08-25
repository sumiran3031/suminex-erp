package com.suminex.erp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "marks_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subject_offering_id", "student_id"})
)
public class MarksEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_offering_id", nullable = false)
    private SubjectOffering subjectOffering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "internal_marks", nullable = false)
    private int internalMarks;

    @Column(name = "external_marks", nullable = false)
    private int externalMarks;

    @Column(name = "practical_marks", nullable = false)
    private int practicalMarks;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private String grade;

    @Column(name = "grade_point", nullable = false)
    private double gradePoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarksEntryStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public MarksEntry() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubjectOffering getSubjectOffering() {
        return subjectOffering;
    }

    public void setSubjectOffering(SubjectOffering subjectOffering) {
        this.subjectOffering = subjectOffering;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(int internalMarks) {
        this.internalMarks = internalMarks;
    }

    public int getExternalMarks() {
        return externalMarks;
    }

    public void setExternalMarks(int externalMarks) {
        this.externalMarks = externalMarks;
    }

    public int getPracticalMarks() {
        return practicalMarks;
    }

    public void setPracticalMarks(int practicalMarks) {
        this.practicalMarks = practicalMarks;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public MarksEntryStatus getStatus() {
        return status;
    }

    public void setStatus(MarksEntryStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}