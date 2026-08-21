package com.suminex.erp.repository;

import com.suminex.erp.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);
}