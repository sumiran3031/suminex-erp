package com.suminex.erp.service;

import com.suminex.erp.dto.BatchRequest;
import com.suminex.erp.dto.BatchResponse;
import com.suminex.erp.entity.Batch;
import com.suminex.erp.entity.Division;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.BatchRepository;
import com.suminex.erp.repository.DivisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final DivisionRepository divisionRepository;

    public BatchService(BatchRepository batchRepository, DivisionRepository divisionRepository) {
        this.batchRepository = batchRepository;
        this.divisionRepository = divisionRepository;
    }

    @Transactional
    public BatchResponse createBatch(BatchRequest request) {
        Division division = divisionRepository.findById(request.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division not found"));

        Batch batch = new Batch();
        batch.setDivision(division);
        batch.setBatchName(request.getBatchName());
        Batch saved = batchRepository.save(batch);

        return toResponse(saved);
    }

    public List<BatchResponse> getByDivision(Long divisionId) {
        return batchRepository.findByDivisionId(divisionId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BatchResponse toResponse(Batch batch) {
        return new BatchResponse(
                batch.getId(),
                batch.getDivision().getId(),
                batch.getDivision().getDivisionName(),
                batch.getBatchName()
        );
    }
}