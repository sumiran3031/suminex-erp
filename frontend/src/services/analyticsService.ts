import apiClient from './apiClient';
import type { AttendanceAnalytics, ResultAnalytics } from '../types/analytics';

export async function getDivisionAttendance(divisionId: number): Promise<AttendanceAnalytics[]> {
  const response = await apiClient.get<AttendanceAnalytics[]>(`/api/analytics/attendance/division/${divisionId}`);
  return response.data;
}

export async function getSubjectResultAnalytics(subjectOfferingId: number): Promise<ResultAnalytics> {
  const response = await apiClient.get<ResultAnalytics>(`/api/analytics/results/subject-offering/${subjectOfferingId}`);
  return response.data;
}