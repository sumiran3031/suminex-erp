package com.suminex.erp.repository;

import com.suminex.erp.entity.CorrectionRequest;
import com.suminex.erp.entity.CorrectionRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorrectionRequestRepository extends JpaRepository<CorrectionRequest, Long> {

    List<CorrectionRequest> findByStatus(CorrectionRequestStatus status);

    List<CorrectionRequest> findByMarksEntryId(Long marksEntryId);
}