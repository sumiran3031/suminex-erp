package com.suminex.erp.repository;

import com.suminex.erp.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findBySubjectOfferingId(Long subjectOfferingId);
}