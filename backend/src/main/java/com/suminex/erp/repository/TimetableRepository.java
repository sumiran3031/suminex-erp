package com.suminex.erp.repository;

import com.suminex.erp.entity.DayOfWeek;
import com.suminex.erp.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findBySubjectOfferingTeacherId(Long teacherId);

    List<Timetable> findBySubjectOfferingDivisionId(Long divisionId);

    List<Timetable> findByDayOfWeekAndTimeSlotIdAndSubjectOfferingTeacherId(
            DayOfWeek dayOfWeek, Long timeSlotId, Long teacherId);

    List<Timetable> findByDayOfWeekAndTimeSlotIdAndRoomId(
            DayOfWeek dayOfWeek, Long timeSlotId, Long roomId);

    List<Timetable> findByDayOfWeekAndTimeSlotIdAndSubjectOfferingDivisionId(
            DayOfWeek dayOfWeek, Long timeSlotId, Long divisionId);
}