export interface AttendanceAnalytics {
  studentId: number;
  studentName: string;
  totalSessions: number;
  presentCount: number;
  attendancePercentage: number;
}

export interface ResultAnalytics {
  subjectName: string;
  totalStudents: number;
  classAverage: number;
  passPercentage: number;
  gradeDistribution: Record<string, number>;
}