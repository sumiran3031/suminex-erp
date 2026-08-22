package com.suminex.erp.service;

import com.suminex.erp.dto.SubjectRequest;
import com.suminex.erp.dto.SubjectResponse;
import com.suminex.erp.entity.CourseProgram;
import com.suminex.erp.entity.Semester;
import com.suminex.erp.entity.Subject;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.CourseProgramRepository;
import com.suminex.erp.repository.SemesterRepository;
import com.suminex.erp.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final CourseProgramRepository courseProgramRepository;
    private final SemesterRepository semesterRepository;

    public SubjectService(SubjectRepository subjectRepository, CourseProgramRepository courseProgramRepository,
                          SemesterRepository semesterRepository) {
        this.subjectRepository = subjectRepository;
        this.courseProgramRepository = courseProgramRepository;
        this.semesterRepository = semesterRepository;
    }

    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            throw new ConflictException("A subject with this code already exists");
        }

        CourseProgram courseProgram = courseProgramRepository.findById(request.getCourseProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("Course program not found"));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        Subject subject = new Subject();
        subject.setCode(request.getCode());
        subject.setName(request.getName());
        subject.setCredits(request.getCredits());
        subject.setSubjectType(request.getSubjectType());
        subject.setCourseProgram(courseProgram);
        subject.setSemester(semester);

        Subject saved = subjectRepository.save(subject);
        return toResponse(saved);
    }

    public List<SubjectResponse> getBySemester(Long semesterId) {
        return subjectRepository.findBySemesterId(semesterId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubjectResponse> getByCourseProgram(Long courseProgramId) {
        return subjectRepository.findByCourseProgramId(courseProgramId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SubjectResponse getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        return toResponse(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found");
        }
        subjectRepository.deleteById(id);
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getCode(),
                subject.getName(),
                subject.getCredits(),
                subject.getSubjectType(),
                subject.getCourseProgram().getId(),
                subject.getCourseProgram().getName(),
                subject.getSemester().getId(),
                subject.getSemester().getSemesterNumber()
        );
    }
}