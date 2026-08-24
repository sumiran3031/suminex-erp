package com.suminex.erp.repository;

import com.suminex.erp.entity.GradingScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradingSchemeRepository extends JpaRepository<GradingScheme, Long> {

    boolean existsByName(String name);

    Optional<GradingScheme> findByActiveTrue();
}