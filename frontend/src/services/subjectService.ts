import apiClient from './apiClient';
import type { Subject, CreateSubjectRequest } from '../types/subject';

export async function getSubjectsBySemester(semesterId: number): Promise<Subject[]> {
  const response = await apiClient.get<Subject[]>(`/api/subjects/by-semester/${semesterId}`);
  return response.data;
}

export async function createSubject(data: CreateSubjectRequest): Promise<Subject> {
  const response = await apiClient.post<Subject>('/api/subjects', data);
  return response.data;
}