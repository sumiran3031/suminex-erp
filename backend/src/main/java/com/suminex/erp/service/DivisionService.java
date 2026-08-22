package com.suminex.erp.service;

import com.suminex.erp.dto.DivisionRequest;
import com.suminex.erp.dto.DivisionResponse;
import com.suminex.erp.entity.Division;
import com.suminex.erp.entity.Semester;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.DivisionRepository;
import com.suminex.erp.repository.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DivisionService {

    private final DivisionRepository divisionRepository;
    private final SemesterRepository semesterRepository;

    public DivisionService(DivisionRepository divisionRepository, SemesterRepository semesterRepository) {
        this.divisionRepository = divisionRepository;
        this.semesterRepository = semesterRepository;
    }

    @Transactional
    public DivisionResponse createDivision(DivisionRequest request) {
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        Division division = new Division();
        division.setSemester(semester);
        division.setDivisionName(request.getDivisionName());
        Division saved = divisionRepository.save(division);

        return toResponse(saved);
    }

    public List<DivisionResponse> getBySemester(Long semesterId) {
        return divisionRepository.findBySemesterId(semesterId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private DivisionResponse toResponse(Division division) {
        return new DivisionResponse(
                division.getId(),
                division.getSemester().getId(),
                division.getSemester().getSemesterNumber(),
                division.getDivisionName()
        );
    }
}