import apiClient from './apiClient';
import type { CreateTeacherRequest, Teacher } from '../types/teacher';

export async function getAllTeachers(): Promise<Teacher[]> {
  const response = await apiClient.get<Teacher[]>('/api/teachers');
  return response.data;
}

export async function createTeacher(data: CreateTeacherRequest): Promise<Teacher> {
  const response = await apiClient.post<Teacher>('/api/teachers', data);
  return response.data;
}