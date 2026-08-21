package com.suminex.erp.repository;

import com.suminex.erp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByPrn(String prn);
}