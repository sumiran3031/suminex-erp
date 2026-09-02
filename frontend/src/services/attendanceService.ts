import apiClient from './apiClient';
import type { MarksEntry } from '../types/marksEntry'; // reuse-style pattern; actually define separately below

export interface MyAttendanceEntry {
  id: number;
  studentId: number;
  studentName: string;
  status: 'PRESENT' | 'ABSENT' | 'LATE';
}

export async function getMyAttendance(): Promise<MyAttendanceEntry[]> {
  const response = await apiClient.get<MyAttendanceEntry[]>('/api/attendance/my-attendance');
  return response.data;
}