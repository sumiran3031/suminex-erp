package com.suminex.erp.repository;

import com.suminex.erp.entity.SubjectOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectOfferingRepository extends JpaRepository<SubjectOffering, Long> {

    boolean existsBySubjectIdAndDivisionIdAndAcademicYearId(Long subjectId, Long divisionId, Long academicYearId);

    List<SubjectOffering> findByTeacherId(Long teacherId);

    List<SubjectOffering> findByDivisionId(Long divisionId);

    List<SubjectOffering> findBySemesterId(Long semesterId);
}