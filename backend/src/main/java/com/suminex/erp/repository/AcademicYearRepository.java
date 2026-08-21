package com.suminex.erp.repository;

import com.suminex.erp.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {

    List<AcademicYear> findByCourseProgramId(Long courseProgramId);
}