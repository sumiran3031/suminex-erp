import apiClient from './apiClient';
import type { Roster, TeachingSession, AttendanceStatus, MyAttendanceEntry } from '../types/attendance';

export async function getMyAttendance(): Promise<MyAttendanceEntry[]> {
  const response = await apiClient.get<MyAttendanceEntry[]>('/api/attendance/my-attendance');
  return response.data;
}

export async function getRoster(teachingSessionId: number): Promise<Roster[]> {
  const response = await apiClient.get<Roster[]>(`/api/attendance/roster/${teachingSessionId}`);
  return response.data;
}

export async function markAttendance(
  teachingSessionId: number,
  entries: { studentId: number; status: AttendanceStatus }[]
) {
  const response = await apiClient.post('/api/attendance', { teachingSessionId, entries });
  return response.data;
}

export async function getSessionsByOffering(subjectOfferingId: number): Promise<TeachingSession[]> {
  const response = await apiClient.get<TeachingSession[]>(`/api/teaching-sessions/by-offering/${subjectOfferingId}`);
  return response.data;
}

export async function createTeachingSession(timetableId: number, sessionDate: string): Promise<TeachingSession> {
  const response = await apiClient.post<TeachingSession>('/api/teaching-sessions', { timetableId, sessionDate });
  return response.data;
}