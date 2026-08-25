package com.suminex.erp.repository;

import com.suminex.erp.entity.MarksEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarksEntryRepository extends JpaRepository<MarksEntry, Long> {

    boolean existsBySubjectOfferingIdAndStudentId(Long subjectOfferingId, Long studentId);

    Optional<MarksEntry> findBySubjectOfferingIdAndStudentId(Long subjectOfferingId, Long studentId);

    List<MarksEntry> findBySubjectOfferingId(Long subjectOfferingId);

    List<MarksEntry> findByStudentId(Long studentId);
}