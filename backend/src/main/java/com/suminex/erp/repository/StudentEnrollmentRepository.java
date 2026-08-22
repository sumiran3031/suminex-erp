package com.suminex.erp.repository;

import com.suminex.erp.entity.EnrollmentStatus;
import com.suminex.erp.entity.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {

    List<StudentEnrollment> findByStudentIdOrderByEnrolledAtDesc(Long studentId);

    Optional<StudentEnrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    List<StudentEnrollment> findByDivisionIdAndStatus(Long divisionId, EnrollmentStatus status);
}