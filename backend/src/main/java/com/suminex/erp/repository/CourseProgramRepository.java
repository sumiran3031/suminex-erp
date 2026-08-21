package com.suminex.erp.repository;

import com.suminex.erp.entity.CourseProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseProgramRepository extends JpaRepository<CourseProgram, Long> {

    boolean existsByCode(String code);

    List<CourseProgram> findByDepartmentId(Long departmentId);
}