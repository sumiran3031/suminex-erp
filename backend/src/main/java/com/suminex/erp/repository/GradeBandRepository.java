package com.suminex.erp.repository;

import com.suminex.erp.entity.GradeBand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeBandRepository extends JpaRepository<GradeBand, Long> {

    List<GradeBand> findByGradingSchemeId(Long gradingSchemeId);

    @org.springframework.data.jpa.repository.Query(
            "SELECT g FROM GradeBand g WHERE g.gradingScheme.id = :schemeId AND :marks BETWEEN g.minMarks AND g.maxMarks"
    )
    Optional<GradeBand> findMatchingBand(Long schemeId, int marks);
}