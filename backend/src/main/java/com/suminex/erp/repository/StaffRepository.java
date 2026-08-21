package com.suminex.erp.repository;

import com.suminex.erp.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByUserId(Long userId);

    boolean existsByEmployeeCode(String employeeCode);
}