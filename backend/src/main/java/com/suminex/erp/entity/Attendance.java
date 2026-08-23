package com.suminex.erp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"teaching_session_id", "student_id"})
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_session_id", nullable = false)
    private TeachingSession teachingSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Column(name = "marked_at", nullable = false, updatable = false)
    private LocalDateTime markedAt;

    @PrePersist
    protected void onCreate() {
        this.markedAt = LocalDateTime.now();
    }

    public Attendance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TeachingSession getTeachingSession() {
        return teachingSession;
    }

    public void setTeachingSession(TeachingSession teachingSession) {
        this.teachingSession = teachingSession;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }
}