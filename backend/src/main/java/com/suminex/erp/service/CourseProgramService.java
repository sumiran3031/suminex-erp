package com.suminex.erp.service;

import com.suminex.erp.dto.CourseProgramRequest;
import com.suminex.erp.dto.CourseProgramResponse;
import com.suminex.erp.entity.CourseProgram;
import com.suminex.erp.entity.Department;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.CourseProgramRepository;
import com.suminex.erp.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseProgramService {

    private final CourseProgramRepository courseProgramRepository;
    private final DepartmentRepository departmentRepository;

    public CourseProgramService(CourseProgramRepository courseProgramRepository,
                                DepartmentRepository departmentRepository) {
        this.courseProgramRepository = courseProgramRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public CourseProgramResponse createCourseProgram(CourseProgramRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (courseProgramRepository.existsByCode(request.getCode())) {
            throw new ConflictException("A course with this code already exists");
        }

        CourseProgram courseProgram = new CourseProgram();
        courseProgram.setDepartment(department);
        courseProgram.setName(request.getName());
        courseProgram.setCode(request.getCode());
        courseProgram.setDurationYears(request.getDurationYears());
        CourseProgram saved = courseProgramRepository.save(courseProgram);

        return toResponse(saved);
    }

    public List<CourseProgramResponse> getAllCoursePrograms() {
        return courseProgramRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CourseProgramResponse> getByDepartment(Long departmentId) {
        return courseProgramRepository.findByDepartmentId(departmentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CourseProgramResponse toResponse(CourseProgram courseProgram) {
        return new CourseProgramResponse(
                courseProgram.getId(),
                courseProgram.getDepartment().getId(),
                courseProgram.getDepartment().getName(),
                courseProgram.getName(),
                courseProgram.getCode(),
                courseProgram.getDurationYears()
        );
    }
}