package com.suminex.erp.repository;

import com.suminex.erp.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUserId(Long userId);

    boolean existsByEmployeeCode(String employeeCode);

    List<Teacher> findByDepartmentId(Long departmentId);
}