package com.suminex.erp.service;

import com.suminex.erp.dto.TimetableRequest;
import com.suminex.erp.dto.TimetableResponse;
import com.suminex.erp.entity.Room;
import com.suminex.erp.entity.SubjectOffering;
import com.suminex.erp.entity.TimeSlot;
import com.suminex.erp.entity.Timetable;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.RoomRepository;
import com.suminex.erp.repository.SubjectOfferingRepository;
import com.suminex.erp.repository.TimeSlotRepository;
import com.suminex.erp.repository.TimetableRepository;
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

    public TimetableService(TimetableRepository timetableRepository,
                            SubjectOfferingRepository subjectOfferingRepository,
                            TimeSlotRepository timeSlotRepository,
                            RoomRepository roomRepository) {
        this.timetableRepository = timetableRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public TimetableResponse createTimetableEntry(TimetableRequest request) {
        // NOTE: no conflict detection yet — this is added on Day 16.
        // Right now this will happily allow double-bookings. Do not treat as production-ready.

        SubjectOffering subjectOffering = subjectOfferingRepository.findById(request.getSubjectOfferingId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject offering not found"));

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Timetable timetable = new Timetable();
        timetable.setSubjectOffering(subjectOffering);
        timetable.setDayOfWeek(request.getDayOfWeek());
        timetable.setTimeSlot(timeSlot);
        timetable.setRoom(room);

        Timetable saved = timetableRepository.save(timetable);
        return toResponse(saved);
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