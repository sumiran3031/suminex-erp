package com.suminex.erp.repository;

import com.suminex.erp.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByTeachingSessionIdAndStudentId(Long teachingSessionId, Long studentId);

    List<Attendance> findByTeachingSessionId(Long teachingSessionId);

    List<Attendance> findByStudentId(Long studentId);
}