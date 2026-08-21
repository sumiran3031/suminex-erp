package com.suminex.erp.service;

import com.suminex.erp.dto.AcademicYearRequest;
import com.suminex.erp.dto.AcademicYearResponse;
import com.suminex.erp.entity.AcademicYear;
import com.suminex.erp.entity.CourseProgram;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.AcademicYearRepository;
import com.suminex.erp.repository.CourseProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final CourseProgramRepository courseProgramRepository;

    public AcademicYearService(AcademicYearRepository academicYearRepository,
                               CourseProgramRepository courseProgramRepository) {
        this.academicYearRepository = academicYearRepository;
        this.courseProgramRepository = courseProgramRepository;
    }

    @Transactional
    public AcademicYearResponse createAcademicYear(AcademicYearRequest request) {
        CourseProgram courseProgram = courseProgramRepository.findById(request.getCourseProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("Course program not found"));

        AcademicYear academicYear = new AcademicYear();
        academicYear.setCourseProgram(courseProgram);
        academicYear.setYearLabel(request.getYearLabel());
        academicYear.setYearNumber(request.getYearNumber());
        AcademicYear saved = academicYearRepository.save(academicYear);

        return toResponse(saved);
    }

    public List<AcademicYearResponse> getByCourseProgram(Long courseProgramId) {
        return academicYearRepository.findByCourseProgramId(courseProgramId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AcademicYearResponse toResponse(AcademicYear academicYear) {
        return new AcademicYearResponse(
                academicYear.getId(),
                academicYear.getCourseProgram().getId(),
                academicYear.getCourseProgram().getName(),
                academicYear.getYearLabel(),
                academicYear.getYearNumber()
        );
    }
}