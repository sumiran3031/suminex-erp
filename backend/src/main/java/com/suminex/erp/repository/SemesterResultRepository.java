package com.suminex.erp.repository;

import com.suminex.erp.entity.SemesterResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SemesterResultRepository extends JpaRepository<SemesterResult, Long> {

    Optional<SemesterResult> findByStudentIdAndSemesterId(Long studentId, Long semesterId);
}