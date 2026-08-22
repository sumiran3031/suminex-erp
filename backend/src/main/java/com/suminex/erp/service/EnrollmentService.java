package com.suminex.erp.service;

import com.suminex.erp.dto.CreateEnrollmentRequest;
import com.suminex.erp.dto.EnrollmentResponse;
import com.suminex.erp.dto.PromoteEnrollmentRequest;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final StudentEnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SemesterRepository semesterRepository;
    private final DivisionRepository divisionRepository;

    public EnrollmentService(StudentEnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             AcademicYearRepository academicYearRepository,
                             SemesterRepository semesterRepository,
                             DivisionRepository divisionRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.academicYearRepository = academicYearRepository;
        this.semesterRepository = semesterRepository;
        this.divisionRepository = divisionRepository;
    }

    @Transactional
    public EnrollmentResponse createEnrollment(CreateEnrollmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (enrollmentRepository.findByStudentIdAndStatus(request.getStudentId(), EnrollmentStatus.ACTIVE).isPresent()) {
            throw new ConflictException("Student already has an active enrollment. Use promote instead.");
        }

        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
        Division division = divisionRepository.findById(request.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division not found"));

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setStudent(student);
        enrollment.setAcademicYear(academicYear);
        enrollment.setSemester(semester);
        enrollment.setDivision(division);
        enrollment.setBatchId(request.getBatchId());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        StudentEnrollment saved = enrollmentRepository.save(enrollment);
        return toResponse(saved);
    }

    @Transactional
    public EnrollmentResponse promoteEnrollment(Long currentEnrollmentId, PromoteEnrollmentRequest request) {
        StudentEnrollment current = enrollmentRepository.findById(currentEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (current.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new BadRequestException("Only an active enrollment can be promoted");
        }

        AcademicYear newAcademicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));
        Semester newSemester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
        Division newDivision = divisionRepository.findById(request.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division not found"));

        // Close the old enrollment — never delete, never overwrite.
        current.setStatus(EnrollmentStatus.PROMOTED);
        current.setEndedAt(LocalDateTime.now());
        enrollmentRepository.save(current);

        // Create the new enrollment as a fresh record.
        StudentEnrollment newEnrollment = new StudentEnrollment();
        newEnrollment.setStudent(current.getStudent());
        newEnrollment.setAcademicYear(newAcademicYear);
        newEnrollment.setSemester(newSemester);
        newEnrollment.setDivision(newDivision);
        newEnrollment.setBatchId(request.getBatchId());
        newEnrollment.setStatus(EnrollmentStatus.ACTIVE);

        StudentEnrollment saved = enrollmentRepository.save(newEnrollment);
        return toResponse(saved);
    }

    public List<EnrollmentResponse> getStudentHistory(Long studentId) {
        return enrollmentRepository.findByStudentIdOrderByEnrolledAtDesc(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getActiveByDivision(Long divisionId) {
        return enrollmentRepository.findByDivisionIdAndStatus(divisionId, EnrollmentStatus.ACTIVE).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private EnrollmentResponse toResponse(StudentEnrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName(),
                enrollment.getAcademicYear().getYearLabel(),
                enrollment.getSemester().getSemesterNumber(),
                enrollment.getDivision().getDivisionName(),
                enrollment.getBatchId(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                enrollment.getEndedAt()
        );
    }
}