package com.suminex.erp.service;

import com.suminex.erp.dto.CorrectionRequestResponse;
import com.suminex.erp.dto.CreateCorrectionRequestDto;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.CorrectionRequestRepository;
import com.suminex.erp.repository.MarksEntryRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorrectionRequestService {

    private final CorrectionRequestRepository correctionRequestRepository;
    private final MarksEntryRepository marksEntryRepository;
    private final GradingSchemeService gradingSchemeService;
    private final AuditLogService auditLogService;

    public CorrectionRequestService(CorrectionRequestRepository correctionRequestRepository,
                                    MarksEntryRepository marksEntryRepository,
                                    GradingSchemeService gradingSchemeService,
                                    AuditLogService auditLogService) {
        this.correctionRequestRepository = correctionRequestRepository;
        this.marksEntryRepository = marksEntryRepository;
        this.gradingSchemeService = gradingSchemeService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public CorrectionRequestResponse createRequest(CreateCorrectionRequestDto dto) {
        MarksEntry entry = marksEntryRepository.findById(dto.getMarksEntryId())
                .orElseThrow(() -> new ResourceNotFoundException("Marks entry not found"));

        if (entry.getStatus() != MarksEntryStatus.PUBLISHED) {
            throw new BadRequestException(
                    "Correction requests can only be made for PUBLISHED marks entries. " +
                            "Current status: " + entry.getStatus() + ". Use the normal edit flow instead.");
        }

        CorrectionRequest request = new CorrectionRequest();
        request.setMarksEntry(entry);
        request.setRequestedByEmail(currentUserEmail());
        request.setReason(dto.getReason());
        request.setProposedInternalMarks(dto.getProposedInternalMarks());
        request.setProposedExternalMarks(dto.getProposedExternalMarks());
        request.setProposedPracticalMarks(dto.getProposedPracticalMarks());
        request.setStatus(CorrectionRequestStatus.PENDING);

        CorrectionRequest saved = correctionRequestRepository.save(request);

        auditLogService.log(
                "REQUEST_MARKS_CORRECTION",
                "MarksEntry",
                entry.getId(),
                "total=" + entry.getTotal(),
                "proposed_total=" + (dto.getProposedInternalMarks() + dto.getProposedExternalMarks() + dto.getProposedPracticalMarks())
        );

        return toResponse(saved);
    }

    @Transactional
    public CorrectionRequestResponse approveRequest(Long id, String reviewNotes) {
        CorrectionRequest request = correctionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Correction request not found"));

        if (request.getStatus() != CorrectionRequestStatus.PENDING) {
            throw new BadRequestException("This request has already been reviewed: " + request.getStatus());
        }

        MarksEntry entry = request.getMarksEntry();

        String oldValue = "internal=" + entry.getInternalMarks() + ", external=" + entry.getExternalMarks()
                + ", practical=" + entry.getPracticalMarks() + ", total=" + entry.getTotal()
                + ", grade=" + entry.getGrade();

        int newTotal = request.getProposedInternalMarks() + request.getProposedExternalMarks()
                + request.getProposedPracticalMarks();
        GradeBand newBand = gradingSchemeService.resolveGrade(newTotal);

        entry.setInternalMarks(request.getProposedInternalMarks());
        entry.setExternalMarks(request.getProposedExternalMarks());
        entry.setPracticalMarks(request.getProposedPracticalMarks());
        entry.setTotal(newTotal);
        entry.setGrade(newBand.getGrade());
        entry.setGradePoint(newBand.getGradePoint());
        marksEntryRepository.save(entry);

        String newValue = "internal=" + entry.getInternalMarks() + ", external=" + entry.getExternalMarks()
                + ", practical=" + entry.getPracticalMarks() + ", total=" + entry.getTotal()
                + ", grade=" + entry.getGrade();

        request.setStatus(CorrectionRequestStatus.APPROVED);
        request.setReviewedByEmail(currentUserEmail());
        request.setReviewNotes(reviewNotes);
        request.setReviewedAt(LocalDateTime.now());
        CorrectionRequest saved = correctionRequestRepository.save(request);

        auditLogService.log(
                "APPROVE_MARKS_CORRECTION",
                "MarksEntry",
                entry.getId(),
                oldValue,
                newValue
        );

        return toResponse(saved);
    }

    @Transactional
    public CorrectionRequestResponse rejectRequest(Long id, String reviewNotes) {
        CorrectionRequest request = correctionRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Correction request not found"));

        if (request.getStatus() != CorrectionRequestStatus.PENDING) {
            throw new BadRequestException("This request has already been reviewed: " + request.getStatus());
        }

        request.setStatus(CorrectionRequestStatus.REJECTED);
        request.setReviewedByEmail(currentUserEmail());
        request.setReviewNotes(reviewNotes);
        request.setReviewedAt(LocalDateTime.now());
        CorrectionRequest saved = correctionRequestRepository.save(request);

        auditLogService.log(
                "REJECT_MARKS_CORRECTION",
                "MarksEntry",
                request.getMarksEntry().getId(),
                null,
                "rejected: " + reviewNotes
        );

        return toResponse(saved);
    }

    public List<CorrectionRequestResponse> getPending() {
        return correctionRequestRepository.findByStatus(CorrectionRequestStatus.PENDING).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private String currentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private CorrectionRequestResponse toResponse(CorrectionRequest request) {
        MarksEntry entry = request.getMarksEntry();
        return new CorrectionRequestResponse(
                request.getId(),
                entry.getId(),
                entry.getStudent().getFirstName() + " " + entry.getStudent().getLastName(),
                entry.getSubjectOffering().getSubject().getName(),
                request.getRequestedByEmail(),
                request.getReason(),
                entry.getTotal(),
                request.getProposedInternalMarks(),
                request.getProposedExternalMarks(),
                request.getProposedPracticalMarks(),
                request.getStatus(),
                request.getReviewedByEmail(),
                request.getReviewNotes(),
                request.getCreatedAt(),
                request.getReviewedAt()
        );
    }
}