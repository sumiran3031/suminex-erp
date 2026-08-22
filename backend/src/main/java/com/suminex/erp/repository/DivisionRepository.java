package com.suminex.erp.repository;

import com.suminex.erp.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DivisionRepository extends JpaRepository<Division, Long> {

    List<Division> findBySemesterId(Long semesterId);
}