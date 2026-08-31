export interface SubjectOffering {
  id: number;
  subjectId: number;
  subjectName: string;
  subjectCode: string;
  teacherId: number;
  teacherName: string;
  academicYearId: number;
  academicYearLabel: string;
  semesterId: number;
  semesterNumber: number;
  divisionId: number;
  divisionName: string;
  batchId: number | null;
}

export interface Room {
  id: number;
  name: string;
  roomType: string | null;
  capacity: number | null;
}

export interface TimeSlot {
  id: number;
  startTime: string;
  endTime: string;
}

export type DayOfWeek = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY';

export interface TimetableEntry {
  id: number;
  subjectOfferingId: number;
  subjectName: string;
  teacherName: string;
  divisionName: string;
  dayOfWeek: DayOfWeek;
  startTime: string;
  endTime: string;
  roomName: string;
}

export interface CreateTimetableRequest {
  subjectOfferingId: number;
  dayOfWeek: DayOfWeek;
  timeSlotId: number;
  roomId: number;
}