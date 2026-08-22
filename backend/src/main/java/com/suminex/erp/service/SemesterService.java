package com.suminex.erp.service;

import com.suminex.erp.dto.SemesterRequest;
import com.suminex.erp.dto.SemesterResponse;
import com.suminex.erp.entity.AcademicYear;
import com.suminex.erp.entity.Semester;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.AcademicYearRepository;
import com.suminex.erp.repository.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;
    private final AcademicYearRepository academicYearRepository;

    public SemesterService(SemesterRepository semesterRepository, AcademicYearRepository academicYearRepository) {
        this.semesterRepository = semesterRepository;
        this.academicYearRepository = academicYearRepository;
    }

    @Transactional
    public SemesterResponse createSemester(SemesterRequest request) {
        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

        Semester semester = new Semester();
        semester.setAcademicYear(academicYear);
        semester.setSemesterNumber(request.getSemesterNumber());
        Semester saved = semesterRepository.save(semester);

        return toResponse(saved);
    }

    public List<SemesterResponse> getByAcademicYear(Long academicYearId) {
        return semesterRepository.findByAcademicYearId(academicYearId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SemesterResponse toResponse(Semester semester) {
        return new SemesterResponse(
                semester.getId(),
                semester.getAcademicYear().getId(),
                semester.getAcademicYear().getYearLabel(),
                semester.getSemesterNumber()
        );
    }
}