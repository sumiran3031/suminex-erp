package com.suminex.erp.service;

import com.suminex.erp.dto.AssignmentResponse;
import com.suminex.erp.dto.AssignmentSubmissionResponse;
import com.suminex.erp.entity.Assignment;
import com.suminex.erp.entity.AssignmentSubmission;
import com.suminex.erp.entity.Student;
import com.suminex.erp.entity.SubjectOffering;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.AssignmentRepository;
import com.suminex.erp.repository.AssignmentSubmissionRepository;
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
    private final FileStorageService fileStorageService;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             AssignmentSubmissionRepository submissionRepository,
                             SubjectOfferingRepository subjectOfferingRepository,
                             StudentRepository studentRepository,
                             FileStorageService fileStorageService) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.studentRepository = studentRepository;
        this.fileStorageService = fileStorageService;
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

        return toResponse(saved);
    }

    @Transactional
    public AssignmentSubmissionResponse submitAssignment(Long assignmentId, Long studentId, MultipartFile file) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, studentId)) {
            throw new ConflictException("You have already submitted this assignment");
        }

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("A file is required to submit");
        }

        String path = fileStorageService.storeSubmissionFile(studentId, file);

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