package com.suminex.erp.service;

import com.suminex.erp.dto.CreateTeachingSessionRequest;
import com.suminex.erp.dto.TeachingSessionResponse;
import com.suminex.erp.entity.SessionStatus;
import com.suminex.erp.entity.TeachingSession;
import com.suminex.erp.entity.Timetable;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.TeachingSessionRepository;
import com.suminex.erp.repository.TimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeachingSessionService {

    private final TeachingSessionRepository teachingSessionRepository;
    private final TimetableRepository timetableRepository;

    public TeachingSessionService(TeachingSessionRepository teachingSessionRepository,
                                  TimetableRepository timetableRepository) {
        this.teachingSessionRepository = teachingSessionRepository;
        this.timetableRepository = timetableRepository;
    }

    @Transactional
    public TeachingSessionResponse createSession(CreateTeachingSessionRequest request) {
        Timetable timetable = timetableRepository.findById(request.getTimetableId())
                .orElseThrow(() -> new ResourceNotFoundException("Timetable entry not found"));

        if (teachingSessionRepository.existsByTimetableIdAndSessionDate(
                request.getTimetableId(), request.getSessionDate())) {
            throw new ConflictException("A session already exists for this timetable entry on this date");
        }

        TeachingSession session = new TeachingSession();
        session.setTimetable(timetable);
        session.setSessionDate(request.getSessionDate());
        session.setStatus(SessionStatus.CONDUCTED);

        TeachingSession saved = teachingSessionRepository.save(session);
        return toResponse(saved);
    }

    public List<TeachingSessionResponse> getByTeacher(Long teacherId) {
        return teachingSessionRepository.findByTimetableSubjectOfferingTeacherId(teacherId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TeachingSessionResponse> getBySubjectOffering(Long subjectOfferingId) {
        return teachingSessionRepository.findByTimetableSubjectOfferingId(subjectOfferingId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TeachingSessionResponse toResponse(TeachingSession session) {
        var offering = session.getTimetable().getSubjectOffering();
        return new TeachingSessionResponse(
                session.getId(),
                session.getTimetable().getId(),
                offering.getSubject().getName(),
                offering.getTeacher().getFirstName() + " " + offering.getTeacher().getLastName(),
                offering.getDivision().getDivisionName(),
                session.getSessionDate(),
                session.getStatus()
        );
    }
}