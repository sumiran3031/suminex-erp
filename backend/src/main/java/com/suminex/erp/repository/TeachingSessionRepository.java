package com.suminex.erp.repository;

import com.suminex.erp.entity.TeachingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeachingSessionRepository extends JpaRepository<TeachingSession, Long> {

    boolean existsByTimetableIdAndSessionDate(Long timetableId, LocalDate sessionDate);

    Optional<TeachingSession> findByTimetableIdAndSessionDate(Long timetableId, LocalDate sessionDate);

    List<TeachingSession> findByTimetableSubjectOfferingTeacherId(Long teacherId);
}