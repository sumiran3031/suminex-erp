package com.suminex.erp.service;

import com.suminex.erp.dto.CreateMarksEntryRequest;
import com.suminex.erp.dto.MarksEntryResponse;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.MarksEntryRepository;
import com.suminex.erp.repository.StudentRepository;
import com.suminex.erp.repository.SubjectOfferingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MarksEntryService {

    private final MarksEntryRepository marksEntryRepository;
    private final SubjectOfferingRepository subjectOfferingRepository;
    private final StudentRepository studentRepository;
    private final GradingSchemeService gradingSchemeService;

    // Defines which status transitions are legal — enforced explicitly, not left implicit.
    private static final Map<MarksEntryStatus, Set<MarksEntryStatus>> ALLOWED_TRANSITIONS = Map.of(
            MarksEntryStatus.DRAFT, Set.of(MarksEntryStatus.SUBMITTED),
            MarksEntryStatus.SUBMITTED, Set.of(MarksEntryStatus.REVIEWED, MarksEntryStatus.DRAFT),
            MarksEntryStatus.REVIEWED, Set.of(MarksEntryStatus.PUBLISHED, MarksEntryStatus.SUBMITTED),
            MarksEntryStatus.PUBLISHED, Set.of() // terminal — no transitions out without a correction flow (future work)
    );

    public MarksEntryService(MarksEntryRepository marksEntryRepository,
                             SubjectOfferingRepository subjectOfferingRepository,
                             StudentRepository studentRepository,
                             GradingSchemeService gradingSchemeService) {
        this.marksEntryRepository = marksEntryRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.studentRepository = studentRepository;
        this.gradingSchemeService = gradingSchemeService;
    }

    @Transactional
    public MarksEntryResponse createMarksEntry(CreateMarksEntryRequest request) {
        SubjectOffering offering = subjectOfferingRepository.findById(request.getSubjectOfferingId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject offering not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (marksEntryRepository.existsBySubjectOfferingIdAndStudentId(
                request.getSubjectOfferingId(), request.getStudentId())) {
            throw new ConflictException("Marks entry already exists for this student in this subject offering");
        }

        int total = request.getInternalMarks() + request.getExternalMarks() + request.getPracticalMarks();
        GradeBand resolvedBand = gradingSchemeService.resolveGrade(total);

        MarksEntry entry = new MarksEntry();
        entry.setSubjectOffering(offering);
        entry.setStudent(student);
        entry.setInternalMarks(request.getInternalMarks());
        entry.setExternalMarks(request.getExternalMarks());
        entry.setPracticalMarks(request.getPracticalMarks());
        entry.setTotal(total);
        entry.setGrade(resolvedBand.getGrade());
        entry.setGradePoint(resolvedBand.getGradePoint());
        entry.setStatus(MarksEntryStatus.DRAFT);

        MarksEntry saved = marksEntryRepository.save(entry);
        return toResponse(saved, resolvedBand.isPass());
    }

    @Transactional
    public MarksEntryResponse updateStatus(Long id, MarksEntryStatus newStatus) {
        MarksEntry entry = marksEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marks entry not found"));

        MarksEntryStatus currentStatus = entry.getStatus();
        Set<MarksEntryStatus> allowedNext = ALLOWED_TRANSITIONS.get(currentStatus);

        if (!allowedNext.contains(newStatus)) {
            throw new BadRequestException(
                    "Cannot transition from " + currentStatus + " to " + newStatus +
                            ". Allowed transitions from " + currentStatus + ": " + allowedNext);
        }

        entry.setStatus(newStatus);
        MarksEntry saved = marksEntryRepository.save(entry);

        GradeBand band = gradingSchemeService.resolveGrade(saved.getTotal());
        return toResponse(saved, band.isPass());
    }

    public List<MarksEntryResponse> getBySubjectOffering(Long subjectOfferingId) {
        return marksEntryRepository.findBySubjectOfferingId(subjectOfferingId).stream()
                .map(entry -> {
                    GradeBand band = gradingSchemeService.resolveGrade(entry.getTotal());
                    return toResponse(entry, band.isPass());
                })
                .collect(Collectors.toList());
    }

    public List<MarksEntryResponse> getByStudent(Long studentId) {
        return marksEntryRepository.findByStudentId(studentId).stream()
                .map(entry -> {
                    GradeBand band = gradingSchemeService.resolveGrade(entry.getTotal());
                    return toResponse(entry, band.isPass());
                })
                .collect(Collectors.toList());
    }

    private MarksEntryResponse toResponse(MarksEntry entry, boolean pass) {
        return new MarksEntryResponse(
                entry.getId(),
                entry.getStudent().getId(),
                entry.getStudent().getFirstName() + " " + entry.getStudent().getLastName(),
                entry.getSubjectOffering().getSubject().getName(),
                entry.getInternalMarks(),
                entry.getExternalMarks(),
                entry.getPracticalMarks(),
                entry.getTotal(),
                entry.getGrade(),
                entry.getGradePoint(),
                pass,
                entry.getStatus()
        );
    }
}