package com.suminex.erp.service;

import com.suminex.erp.dto.AttendanceAnalyticsResponse;
import com.suminex.erp.dto.ResultAnalyticsResponse;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final AttendanceRepository attendanceRepository;
    private final StudentEnrollmentRepository enrollmentRepository;
    private final MarksEntryRepository marksEntryRepository;
    private final SubjectOfferingRepository subjectOfferingRepository;
    private final GradingSchemeRepository gradingSchemeRepository;

    public AnalyticsService(AttendanceRepository attendanceRepository,
                            StudentEnrollmentRepository enrollmentRepository,
                            MarksEntryRepository marksEntryRepository,
                            SubjectOfferingRepository subjectOfferingRepository,
                            GradingSchemeRepository gradingSchemeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.marksEntryRepository = marksEntryRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.gradingSchemeRepository = gradingSchemeRepository;
    }

    /**
     * Attendance analytics per student, for a given division — active enrollments only.
     */
    public List<AttendanceAnalyticsResponse> getDivisionAttendanceAnalytics(Long divisionId) {
        List<StudentEnrollment> activeEnrollments = enrollmentRepository
                .findByDivisionIdAndStatus(divisionId, EnrollmentStatus.ACTIVE);

        return activeEnrollments.stream()
                .map(enrollment -> {
                    Student student = enrollment.getStudent();
                    List<Attendance> records = attendanceRepository.findByStudentId(student.getId());

                    long total = records.size();
                    long present = records.stream()
                            .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                            .count();

                    double percentage = total > 0 ? Math.round((present * 10000.0 / total)) / 100.0 : 0;

                    return new AttendanceAnalyticsResponse(
                            student.getId(),
                            student.getFirstName() + " " + student.getLastName(),
                            total,
                            present,
                            percentage
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Result analytics for a specific subject offering — considers only PUBLISHED marks entries,
     * consistent with how SGPA calculation treats "final" results.
     */
    public ResultAnalyticsResponse getSubjectResultAnalytics(Long subjectOfferingId) {
        SubjectOffering offering = subjectOfferingRepository.findById(subjectOfferingId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject offering not found"));

        List<MarksEntry> publishedEntries = marksEntryRepository.findBySubjectOfferingId(subjectOfferingId).stream()
                .filter(e -> e.getStatus() == MarksEntryStatus.PUBLISHED)
                .collect(Collectors.toList());

        if (publishedEntries.isEmpty()) {
            return new ResultAnalyticsResponse(
                    offering.getSubject().getName(), 0, 0, 0, Map.of());
        }

        double average = publishedEntries.stream()
                .mapToInt(MarksEntry::getTotal)
                .average()
                .orElse(0);
        average = Math.round(average * 100.0) / 100.0;

        // Resolve pass/fail per entry by looking up each grade band's pass flag via the active scheme.
        GradingScheme activeScheme = gradingSchemeRepository.findByActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active grading scheme configured"));

        long passCount = publishedEntries.stream()
                .filter(entry -> activeScheme.getGradeBands().stream()
                        .anyMatch(band -> band.getGrade().equals(entry.getGrade()) && band.isPass()))
                .count();

        double passPercentage = Math.round((passCount * 10000.0 / publishedEntries.size())) / 100.0;

        Map<String, Long> gradeDistribution = publishedEntries.stream()
                .collect(Collectors.groupingBy(MarksEntry::getGrade, Collectors.counting()));

        return new ResultAnalyticsResponse(
                offering.getSubject().getName(),
                publishedEntries.size(),
                average,
                passPercentage,
                gradeDistribution
        );
    }
}