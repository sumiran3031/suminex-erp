package com.suminex.erp.service;

import com.suminex.erp.dto.CreateMarksEntryRequest;
import com.suminex.erp.dto.MarksEntryResponse;
import com.suminex.erp.dto.SessionRosterResponse;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.MarksEntryRepository;
import com.suminex.erp.repository.StudentEnrollmentRepository;
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
    private final StudentEnrollmentRepository enrollmentRepository;
    private final GradingSchemeService gradingSchemeService;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    private static final Map<MarksEntryStatus, Set<MarksEntryStatus>>
            ALLOWED_TRANSITIONS = Map.of(
            MarksEntryStatus.DRAFT,
            Set.of(MarksEntryStatus.SUBMITTED),

            MarksEntryStatus.SUBMITTED,
            Set.of(
                    MarksEntryStatus.REVIEWED,
                    MarksEntryStatus.DRAFT
            ),

            MarksEntryStatus.REVIEWED,
            Set.of(
                    MarksEntryStatus.PUBLISHED,
                    MarksEntryStatus.SUBMITTED
            ),

            MarksEntryStatus.PUBLISHED,
            Set.of()
    );

    public MarksEntryService(
            MarksEntryRepository marksEntryRepository,
            SubjectOfferingRepository subjectOfferingRepository,
            StudentRepository studentRepository,
            StudentEnrollmentRepository enrollmentRepository,
            GradingSchemeService gradingSchemeService,
            NotificationService notificationService,
            AuditLogService auditLogService) {

        this.marksEntryRepository = marksEntryRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.gradingSchemeService = gradingSchemeService;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public MarksEntryResponse createMarksEntry(
            CreateMarksEntryRequest request) {

        SubjectOffering offering =
                subjectOfferingRepository.findById(
                                request.getSubjectOfferingId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subject offering not found"
                                ));

        Student student =
                studentRepository.findById(
                                request.getStudentId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found"
                                ));

        if (marksEntryRepository
                .existsBySubjectOfferingIdAndStudentId(
                        request.getSubjectOfferingId(),
                        request.getStudentId())) {

            throw new ConflictException(
                    "Marks entry already exists for this student "
                            + "in this subject offering"
            );
        }

        int total =
                request.getInternalMarks()
                        + request.getExternalMarks()
                        + request.getPracticalMarks();

        GradeBand resolvedBand =
                gradingSchemeService.resolveGrade(total);

        MarksEntry entry = new MarksEntry();

        entry.setSubjectOffering(offering);
        entry.setStudent(student);

        entry.setInternalMarks(
                request.getInternalMarks()
        );

        entry.setExternalMarks(
                request.getExternalMarks()
        );

        entry.setPracticalMarks(
                request.getPracticalMarks()
        );

        entry.setTotal(total);
        entry.setGrade(resolvedBand.getGrade());
        entry.setGradePoint(
                resolvedBand.getGradePoint()
        );

        entry.setStatus(
                MarksEntryStatus.DRAFT
        );

        MarksEntry saved =
                marksEntryRepository.save(entry);

        auditLogService.log(
                "CREATE_MARKS_ENTRY",
                "MarksEntry",
                saved.getId(),
                null,
                "total=" + total
                        + ", grade="
                        + resolvedBand.getGrade()
        );

        return toResponse(
                saved,
                resolvedBand.isPass()
        );
    }

    @Transactional
    public MarksEntryResponse updateStatus(
            Long id,
            MarksEntryStatus newStatus) {

        MarksEntry entry =
                marksEntryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Marks entry not found"
                                ));

        MarksEntryStatus currentStatus =
                entry.getStatus();

        Set<MarksEntryStatus> allowedNext =
                ALLOWED_TRANSITIONS.get(currentStatus);

        if (!allowedNext.contains(newStatus)) {

            throw new BadRequestException(
                    "Cannot transition from "
                            + currentStatus
                            + " to "
                            + newStatus
                            + ". Allowed transitions from "
                            + currentStatus
                            + ": "
                            + allowedNext
            );
        }

        entry.setStatus(newStatus);

        MarksEntry saved =
                marksEntryRepository.save(entry);

        auditLogService.log(
                "UPDATE_MARKS_STATUS",
                "MarksEntry",
                saved.getId(),
                currentStatus.toString(),
                newStatus.toString()
        );

        if (newStatus ==
                MarksEntryStatus.PUBLISHED) {

            String message =
                    "Your result for "
                            + saved.getSubjectOffering()
                            .getSubject()
                            .getName()
                            + " has been published. Grade: "
                            + saved.getGrade();

            notificationService.createNotification(
                    saved.getStudent().getUser(),
                    NotificationType.RESULT_PUBLISHED,
                    message
            );
        }

        GradeBand band =
                gradingSchemeService.resolveGrade(
                        saved.getTotal()
                );

        return toResponse(
                saved,
                band.isPass()
        );
    }

    public List<MarksEntryResponse>
    getBySubjectOffering(
            Long subjectOfferingId) {

        return marksEntryRepository
                .findBySubjectOfferingId(
                        subjectOfferingId
                )
                .stream()
                .map(entry -> {

                    GradeBand band =
                            gradingSchemeService.resolveGrade(
                                    entry.getTotal()
                            );

                    return toResponse(
                            entry,
                            band.isPass()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<MarksEntryResponse>
    getByStudent(Long studentId) {

        return marksEntryRepository
                .findByStudentId(studentId)
                .stream()
                .map(entry -> {

                    GradeBand band =
                            gradingSchemeService.resolveGrade(
                                    entry.getTotal()
                            );

                    return toResponse(
                            entry,
                            band.isPass()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns results only for the currently authenticated student.
     *
     * IMPORTANT:
     * Only PUBLISHED marks are exposed to students.
     * DRAFT, SUBMITTED and REVIEWED marks remain hidden.
     */
    public List<MarksEntryResponse>
    getMyResults(Long userId) {

        Student student =
                studentRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No student profile found for this account"
                                ));

        return getByStudent(student.getId())
                .stream()
                .filter(entry ->
                        entry.getStatus()
                                == MarksEntryStatus.PUBLISHED
                )
                .collect(Collectors.toList());
    }

    public List<SessionRosterResponse>
    getEligibleStudents(Long subjectOfferingId) {

        SubjectOffering offering =
                subjectOfferingRepository.findById(
                                subjectOfferingId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subject offering not found"
                                ));

        return enrollmentRepository
                .findByDivisionIdAndStatus(
                        offering.getDivision().getId(),
                        EnrollmentStatus.ACTIVE
                )
                .stream()
                .map(e -> new SessionRosterResponse(
                        e.getStudent().getId(),
                        e.getStudent().getFirstName()
                                + " "
                                + e.getStudent().getLastName(),
                        e.getStudent().getRollNumber()
                ))
                .collect(Collectors.toList());
    }

    private MarksEntryResponse toResponse(
            MarksEntry entry,
            boolean pass) {

        return new MarksEntryResponse(
                entry.getId(),
                entry.getStudent().getId(),
                entry.getStudent().getFirstName()
                        + " "
                        + entry.getStudent().getLastName(),
                entry.getSubjectOffering()
                        .getSubject()
                        .getName(),
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