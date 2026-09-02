package com.suminex.erp.service;

import com.suminex.erp.dto.*;
import com.suminex.erp.entity.*;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.AttendanceRepository;
import com.suminex.erp.repository.StudentEnrollmentRepository;
import com.suminex.erp.repository.StudentRepository;
import com.suminex.erp.repository.TeachingSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final TeachingSessionRepository teachingSessionRepository;
    private final StudentEnrollmentRepository enrollmentRepository;

    // Added for student's own attendance
    private final StudentRepository studentRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            TeachingSessionRepository teachingSessionRepository,
            StudentEnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository) {

        this.attendanceRepository = attendanceRepository;
        this.teachingSessionRepository = teachingSessionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Returns the correct roster for a session:
     * batch-wise if the SubjectOffering has a batch,
     * division-wise otherwise.
     */
    public List<SessionRosterResponse> getRosterForSession(
            Long teachingSessionId) {

        TeachingSession session =
                teachingSessionRepository.findById(teachingSessionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teaching session not found"
                                ));

        SubjectOffering offering =
                session.getTimetable().getSubjectOffering();

        Long divisionId =
                offering.getDivision().getId();

        List<StudentEnrollment> activeEnrollments =
                enrollmentRepository.findByDivisionIdAndStatus(
                        divisionId,
                        EnrollmentStatus.ACTIVE
                );

        if (offering.getBatch() != null) {

            Long batchId =
                    offering.getBatch().getId();

            activeEnrollments =
                    activeEnrollments.stream()
                            .filter(e ->
                                    e.getBatch() != null
                                            && e.getBatch()
                                            .getId()
                                            .equals(batchId)
                            )
                            .collect(Collectors.toList());
        }

        return activeEnrollments.stream()
                .map(e -> new SessionRosterResponse(
                        e.getStudent().getId(),
                        e.getStudent().getFirstName()
                                + " "
                                + e.getStudent().getLastName(),
                        e.getStudent().getRollNumber()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Returns attendance records belonging only to
     * the currently authenticated student.
     */
    public List<AttendanceResponse> getMyAttendance(
            Long userId) {

        Student student =
                studentRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No student profile found for this account"
                                ));

        return attendanceRepository
                .findByStudentId(student.getId())
                .stream()
                .map(a -> new AttendanceResponse(
                        a.getId(),
                        a.getStudent().getId(),
                        a.getStudent().getFirstName()
                                + " "
                                + a.getStudent().getLastName(),
                        a.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AttendanceResponse> markAttendance(
            MarkAttendanceRequest request) {

        TeachingSession session =
                teachingSessionRepository.findById(
                                request.getTeachingSessionId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teaching session not found"
                                ));

        List<SessionRosterResponse> validRoster =
                getRosterForSession(
                        request.getTeachingSessionId()
                );

        List<Long> validStudentIds =
                validRoster.stream()
                        .map(SessionRosterResponse::getStudentId)
                        .collect(Collectors.toList());

        for (AttendanceEntryRequest entry :
                request.getEntries()) {

            if (!validStudentIds.contains(
                    entry.getStudentId())) {

                throw new BadRequestException(
                        "Student id "
                                + entry.getStudentId()
                                + " is not part of this session's roster"
                );
            }

            if (attendanceRepository
                    .existsByTeachingSessionIdAndStudentId(
                            request.getTeachingSessionId(),
                            entry.getStudentId())) {

                throw new ConflictException(
                        "Attendance already marked for student id "
                                + entry.getStudentId()
                                + " in this session"
                );
            }
        }

        return request.getEntries()
                .stream()
                .map(entry -> {

                    Attendance attendance =
                            new Attendance();

                    attendance.setTeachingSession(session);

                    Student student =
                            findStudentFromRoster(
                                    entry.getStudentId(),
                                    request.getTeachingSessionId()
                            );

                    attendance.setStudent(student);
                    attendance.setStatus(entry.getStatus());

                    Attendance saved =
                            attendanceRepository.save(attendance);

                    return new AttendanceResponse(
                            saved.getId(),
                            student.getId(),
                            student.getFirstName()
                                    + " "
                                    + student.getLastName(),
                            saved.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    private Student findStudentFromRoster(
            Long studentId,
            Long teachingSessionId) {

        TeachingSession session =
                teachingSessionRepository.findById(
                                teachingSessionId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teaching session not found"
                                ));

        SubjectOffering offering =
                session.getTimetable()
                        .getSubjectOffering();

        Long divisionId =
                offering.getDivision().getId();

        return enrollmentRepository
                .findByDivisionIdAndStatus(
                        divisionId,
                        EnrollmentStatus.ACTIVE
                )
                .stream()
                .map(StudentEnrollment::getStudent)
                .filter(s ->
                        s.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found in roster"
                        ));
    }

    public List<AttendanceResponse> getBySession(
            Long teachingSessionId) {

        return attendanceRepository
                .findByTeachingSessionId(teachingSessionId)
                .stream()
                .map(a -> new AttendanceResponse(
                        a.getId(),
                        a.getStudent().getId(),
                        a.getStudent().getFirstName()
                                + " "
                                + a.getStudent().getLastName(),
                        a.getStatus()
                ))
                .collect(Collectors.toList());
    }
}