export interface Roster {
  studentId: number;
  studentName: string;
  rollNumber: string;
}

export interface TeachingSession {
  id: number;
  timetableId: number;
  subjectName: string;
  teacherName: string;
  divisionName: string;
  sessionDate: string;
  status: 'CONDUCTED' | 'CANCELLED';
}

export type AttendanceStatus = 'PRESENT' | 'ABSENT' | 'LATE';

export interface MyAttendanceEntry {
  id: number;
  studentId: number;
  studentName: string;
  status: AttendanceStatus;
}