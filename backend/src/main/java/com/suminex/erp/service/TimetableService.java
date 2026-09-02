package com.suminex.erp.service;

import com.suminex.erp.dto.TimetableRequest;
import com.suminex.erp.dto.TimetableResponse;
import com.suminex.erp.entity.Room;
import com.suminex.erp.entity.SubjectOffering;
import com.suminex.erp.entity.TimeSlot;
import com.suminex.erp.entity.Timetable;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.RoomRepository;
import com.suminex.erp.repository.StudentEnrollmentRepository;
import com.suminex.erp.repository.StudentRepository;
import com.suminex.erp.repository.SubjectOfferingRepository;
import com.suminex.erp.repository.TimeSlotRepository;
import com.suminex.erp.repository.TimetableRepository;
import com.suminex.erp.entity.EnrollmentStatus;
import com.suminex.erp.entity.Student;
import com.suminex.erp.entity.StudentEnrollment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final SubjectOfferingRepository subjectOfferingRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;
    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository enrollmentRepository;

    public TimetableService(TimetableRepository timetableRepository,
                            SubjectOfferingRepository subjectOfferingRepository,
                            TimeSlotRepository timeSlotRepository,
                            RoomRepository roomRepository,
                            StudentRepository studentRepository,
                            StudentEnrollmentRepository enrollmentRepository) {
        this.timetableRepository = timetableRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public TimetableResponse createTimetableEntry(TimetableRequest request) {
        SubjectOffering subjectOffering = subjectOfferingRepository.findById(request.getSubjectOfferingId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject offering not found"));

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Long teacherId = subjectOffering.getTeacher().getId();
        Long divisionId = subjectOffering.getDivision().getId();

        checkTeacherConflict(request, teacherId);
        checkRoomConflict(request);
        checkDivisionConflict(request, divisionId);

        Timetable timetable = new Timetable();
        timetable.setSubjectOffering(subjectOffering);
        timetable.setDayOfWeek(request.getDayOfWeek());
        timetable.setTimeSlot(timeSlot);
        timetable.setRoom(room);

        Timetable saved = timetableRepository.save(timetable);
        return toResponse(saved);
    }

    private void checkTeacherConflict(TimetableRequest request, Long teacherId) {
        List<Timetable> existing = timetableRepository
                .findByDayOfWeekAndTimeSlotIdAndSubjectOfferingTeacherId(
                        request.getDayOfWeek(), request.getTimeSlotId(), teacherId);

        if (!existing.isEmpty()) {
            Timetable conflict = existing.get(0);
            throw new ConflictException(
                    "Teacher conflict: " + conflict.getSubjectOffering().getTeacher().getFirstName() + " "
                            + conflict.getSubjectOffering().getTeacher().getLastName()
                            + " is already scheduled for " + conflict.getSubjectOffering().getSubject().getName()
                            + " on " + request.getDayOfWeek() + " at this time slot"
            );
        }
    }

    private void checkRoomConflict(TimetableRequest request) {
        List<Timetable> existing = timetableRepository
                .findByDayOfWeekAndTimeSlotIdAndRoomId(
                        request.getDayOfWeek(), request.getTimeSlotId(), request.getRoomId());

        if (!existing.isEmpty()) {
            Timetable conflict = existing.get(0);
            throw new ConflictException(
                    "Room conflict: " + conflict.getRoom().getName()
                            + " is already booked for " + conflict.getSubjectOffering().getSubject().getName()
                            + " on " + request.getDayOfWeek() + " at this time slot"
            );
        }
    }

    private void checkDivisionConflict(TimetableRequest request, Long divisionId) {
        List<Timetable> existing = timetableRepository
                .findByDayOfWeekAndTimeSlotIdAndSubjectOfferingDivisionId(
                        request.getDayOfWeek(), request.getTimeSlotId(), divisionId);

        if (!existing.isEmpty()) {
            Timetable conflict = existing.get(0);
            throw new ConflictException(
                    "Division conflict: " + conflict.getSubjectOffering().getDivision().getDivisionName()
                            + " already has " + conflict.getSubjectOffering().getSubject().getName()
                            + " scheduled on " + request.getDayOfWeek() + " at this time slot"
            );
        }
    }

    public List<TimetableResponse> getByTeacher(Long teacherId) {
        return timetableRepository.findBySubjectOfferingTeacherId(teacherId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TimetableResponse> getByDivision(Long divisionId) {
        return timetableRepository.findBySubjectOfferingDivisionId(divisionId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TimetableResponse> getBySubjectOffering(Long subjectOfferingId) {
        return timetableRepository.findBySubjectOfferingId(subjectOfferingId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TimetableResponse> getMyTimetable(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No student profile found for this account"));

        StudentEnrollment activeEnrollment = enrollmentRepository
                .findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active enrollment found"));

        return getByDivision(activeEnrollment.getDivision().getId());
    }

    private TimetableResponse toResponse(Timetable timetable) {
        SubjectOffering offering = timetable.getSubjectOffering();
        return new TimetableResponse(
                timetable.getId(),
                offering.getId(),
                offering.getSubject().getName(),
                offering.getTeacher().getFirstName() + " " + offering.getTeacher().getLastName(),
                offering.getDivision().getDivisionName(),
                timetable.getDayOfWeek(),
                timetable.getTimeSlot().getStartTime(),
                timetable.getTimeSlot().getEndTime(),
                timetable.getRoom().getName()
        );
    }
}