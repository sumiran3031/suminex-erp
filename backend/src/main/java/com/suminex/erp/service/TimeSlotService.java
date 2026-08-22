package com.suminex.erp.service;

import com.suminex.erp.dto.TimeSlotRequest;
import com.suminex.erp.dto.TimeSlotResponse;
import com.suminex.erp.entity.TimeSlot;
import com.suminex.erp.exception.BadRequestException;
import com.suminex.erp.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Transactional
    public TimeSlotResponse createTimeSlot(TimeSlotRequest request) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BadRequestException("End time must be after start time");
        }

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        TimeSlot saved = timeSlotRepository.save(timeSlot);

        return toResponse(saved);
    }

    public List<TimeSlotResponse> getAllTimeSlots() {
        return timeSlotRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TimeSlotResponse toResponse(TimeSlot timeSlot) {
        return new TimeSlotResponse(timeSlot.getId(), timeSlot.getStartTime(), timeSlot.getEndTime());
    }
}