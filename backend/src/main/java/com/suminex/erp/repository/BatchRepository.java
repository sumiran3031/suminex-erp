package com.suminex.erp.repository;

import com.suminex.erp.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findByDivisionId(Long divisionId);
}