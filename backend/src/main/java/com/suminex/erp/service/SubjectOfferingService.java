package com.suminex.erp.service;

import com.suminex.erp.dto.SubjectOfferingRequest;
import com.suminex.erp.dto.SubjectOfferingResponse;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectOfferingService {

    private final SubjectOfferingRepository subjectOfferingRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SemesterRepository semesterRepository;
    private final DivisionRepository divisionRepository;
    private final BatchRepository batchRepository;

    public SubjectOfferingService(SubjectOfferingRepository subjectOfferingRepository,
                                  SubjectRepository subjectRepository,
                                  TeacherRepository teacherRepository,
                                  AcademicYearRepository academicYearRepository,
                                  SemesterRepository semesterRepository,
                                  DivisionRepository divisionRepository,
                                  BatchRepository batchRepository) {
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.academicYearRepository = academicYearRepository;
        this.semesterRepository = semesterRepository;
        this.divisionRepository = divisionRepository;
        this.batchRepository = batchRepository;
    }

    @Transactional
    public SubjectOfferingResponse createSubjectOffering(SubjectOfferingRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        Division division = divisionRepository.findById(request.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division not found"));

        if (subjectOfferingRepository.existsBySubjectIdAndDivisionIdAndAcademicYearId(
                request.getSubjectId(), request.getDivisionId(), request.getAcademicYearId())) {
            throw new ConflictException(
                    "This subject is already offered for this division in this academic year");
        }

        SubjectOffering offering = new SubjectOffering();
        offering.setSubject(subject);
        offering.setTeacher(teacher);
        offering.setAcademicYear(academicYear);
        offering.setSemester(semester);
        offering.setDivision(division);

        if (request.getBatchId() != null) {
            Batch batch = batchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
            offering.setBatch(batch);
        }

        SubjectOffering saved = subjectOfferingRepository.save(offering);
        return toResponse(saved);
    }

    public List<SubjectOfferingResponse> getByTeacher(Long teacherId) {
        return subjectOfferingRepository.findByTeacherId(teacherId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubjectOfferingResponse> getByDivision(Long divisionId) {
        return subjectOfferingRepository.findByDivisionId(divisionId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubjectOfferingResponse> getBySemester(Long semesterId) {
        return subjectOfferingRepository.findBySemesterId(semesterId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SubjectOfferingResponse toResponse(SubjectOffering offering) {
        return new SubjectOfferingResponse(
                offering.getId(),
                offering.getSubject().getId(),
                offering.getSubject().getName(),
                offering.getSubject().getCode(),
                offering.getTeacher().getId(),
                offering.getTeacher().getFirstName() + " " + offering.getTeacher().getLastName(),
                offering.getAcademicYear().getId(),
                offering.getAcademicYear().getYearLabel(),
                offering.getSemester().getId(),
                offering.getSemester().getSemesterNumber(),
                offering.getDivision().getId(),
                offering.getDivision().getDivisionName(),
                offering.getBatch() != null ? offering.getBatch().getId() : null
        );
    }
}