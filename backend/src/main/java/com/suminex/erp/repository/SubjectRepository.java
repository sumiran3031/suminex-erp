package com.suminex.erp.repository;

import com.suminex.erp.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsByCode(String code);

    List<Subject> findBySemesterId(Long semesterId);

    List<Subject> findByCourseProgramId(Long courseProgramId);

    @Query("SELECT s FROM Subject s WHERE s.courseProgram.department.id = :departmentId")
    List<Subject> findByCourseProgramDepartmentId(Long departmentId);
}