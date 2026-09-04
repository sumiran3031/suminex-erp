package com.suminex.erp.service;

import com.suminex.erp.dto.AssignmentResponse;
import com.suminex.erp.dto.AssignmentSubmissionResponse;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.AssignmentRepository;
import com.suminex.erp.repository.AssignmentSubmissionRepository;
import com.suminex.erp.repository.StudentEnrollmentRepository;
import com.suminex.erp.repository.StudentRepository;
import com.suminex.erp.repository.SubjectOfferingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final SubjectOfferingRepository subjectOfferingRepository;
    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository enrollmentRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             AssignmentSubmissionRepository submissionRepository,
                             SubjectOfferingRepository subjectOfferingRepository,
                             StudentRepository studentRepository,
                             StudentEnrollmentRepository enrollmentRepository,
                             FileStorageService fileStorageService,
                             NotificationService notificationService) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }

    @Transactional
    public AssignmentResponse createAssignment(Long subjectOfferingId, String title, String description,
                                               LocalDateTime dueDate, MultipartFile file) {
        SubjectOffering offering = subjectOfferingRepository.findById(subjectOfferingId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject offering not found"));

        Assignment assignment = new Assignment();
        assignment.setSubjectOffering(offering);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);

        Assignment saved = assignmentRepository.save(assignment);

        if (file != null && !file.isEmpty()) {
            String path = fileStorageService.storeAssignmentFile(saved.getId(), file);
            saved.setFilePath(path);
            saved = assignmentRepository.save(saved);
        }

        notifyDivisionStudents(offering, saved);

        return toResponse(saved);
    }

    private void notifyDivisionStudents(SubjectOffering offering, Assignment assignment) {
        List<StudentEnrollment> activeEnrollments = enrollmentRepository
                .findByDivisionIdAndStatus(offering.getDivision().getId(), EnrollmentStatus.ACTIVE);

        String message = "New assignment posted: \"" + assignment.getTitle() + "\" for "
                + offering.getSubject().getName() + ". Due: " + assignment.getDueDate();

        for (StudentEnrollment enrollment : activeEnrollments) {
            notificationService.createNotification(
                    enrollment.getStudent().getUser(),
                    NotificationType.NEW_ASSIGNMENT,
                    message
            );
        }
    }

    @Transactional
    public AssignmentSubmissionResponse submitAssignment(Long assignmentId, Long userId, MultipartFile file) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No student profile found for this account. Only students can submit assignments."));

        if (submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, student.getId())) {
            throw new ConflictException("You have already submitted this assignment");
        }

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("A file is required to submit");
        }

        String path = fileStorageService.storeSubmissionFile(student.getId(), file);

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFilePath(path);

        AssignmentSubmission saved = submissionRepository.save(submission);

        return toSubmissionResponse(saved);
    }

    public List<AssignmentResponse> getBySubjectOffering(Long subjectOfferingId) {
        return assignmentRepository.findBySubjectOfferingId(subjectOfferingId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Returns assignments for the subject offerings tied to the student's own
     * active division enrollment — resolved from their token, never a client id.
     */
    public List<AssignmentResponse> getMyAssignments(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No student profile found for this account"));

        StudentEnrollment activeEnrollment = enrollmentRepository
                .findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active enrollment found"));

        List<SubjectOffering> offerings = subjectOfferingRepository
                .findByDivisionId(activeEnrollment.getDivision().getId());

        return offerings.stream()
                .flatMap(o -> assignmentRepository.findBySubjectOfferingId(o.getId()).stream())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * For a given student's own view: has THIS student already submitted THIS assignment?
     */
    public boolean hasSubmitted(Long assignmentId, Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No student profile found for this account"));
        return submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, student.getId());
    }

    public List<AssignmentSubmissionResponse> getSubmissions(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId).stream()
                .map(this::toSubmissionResponse)
                .collect(Collectors.toList());
    }

    private AssignmentResponse toResponse(Assignment assignment) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getSubjectOffering().getSubject().getName(),
                assignment.getDueDate(),
                assignment.getFilePath()
        );
    }

    private AssignmentSubmissionResponse toSubmissionResponse(AssignmentSubmission submission) {
        return new AssignmentSubmissionResponse(
                submission.getId(),
                submission.getStudent().getId(),
                submission.getStudent().getFirstName() + " " + submission.getStudent().getLastName(),
                submission.getFilePath(),
                submission.getSubmittedAt()
        );
    }
}