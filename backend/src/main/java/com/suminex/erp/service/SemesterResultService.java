package com.suminex.erp.service;

import com.suminex.erp.dto.SemesterResultResponse;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SemesterResultService {

    private final SemesterResultRepository semesterResultRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final MarksEntryRepository marksEntryRepository;

    public SemesterResultService(SemesterResultRepository semesterResultRepository,
                                 StudentRepository studentRepository,
                                 SemesterRepository semesterRepository,
                                 MarksEntryRepository marksEntryRepository) {
        this.semesterResultRepository = semesterResultRepository;
        this.studentRepository = studentRepository;
        this.semesterRepository = semesterRepository;
        this.marksEntryRepository = marksEntryRepository;
    }

    @Transactional
    public SemesterResultResponse calculateSgpa(Long studentId, Long semesterId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        List<MarksEntry> allEntries = marksEntryRepository.findByStudentId(studentId);

        // Only consider entries for subject offerings within this semester, and only PUBLISHED ones.
        List<MarksEntry> relevantEntries = allEntries.stream()
                .filter(e -> e.getSubjectOffering().getSemester().getId().equals(semesterId))
                .filter(e -> e.getStatus() == MarksEntryStatus.PUBLISHED)
                .toList();

        if (relevantEntries.isEmpty()) {
            throw new BadRequestException(
                    "No published marks entries found for this student in this semester. SGPA cannot be calculated.");
        }

        double weightedSum = 0;
        int totalCredits = 0;

        for (MarksEntry entry : relevantEntries) {
            int credits = entry.getSubjectOffering().getSubject().getCredits();
            weightedSum += entry.getGradePoint() * credits;
            totalCredits += credits;
        }

        double sgpa = totalCredits > 0 ? weightedSum / totalCredits : 0;
        // Round to 2 decimal places for a clean, standard SGPA display.
        sgpa = Math.round(sgpa * 100.0) / 100.0;

        SemesterResult result = semesterResultRepository.findByStudentIdAndSemesterId(studentId, semesterId)
                .orElseGet(SemesterResult::new);

        result.setStudent(student);
        result.setSemester(semester);
        result.setSgpa(sgpa);
        result.setStatus("CALCULATED");

        SemesterResult saved = semesterResultRepository.save(result);
        return toResponse(saved);
    }

    private SemesterResultResponse toResponse(SemesterResult result) {
        return new SemesterResultResponse(
                result.getId(),
                result.getStudent().getId(),
                result.getStudent().getFirstName() + " " + result.getStudent().getLastName(),
                result.getSemester().getSemesterNumber(),
                result.getSgpa(),
                result.getStatus(),
                result.getCalculatedAt()
        );
    }
}