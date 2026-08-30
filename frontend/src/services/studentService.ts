import apiClient from './apiClient';
import type { Student, CreateStudentRequest } from '../types/student';

export async function getAllStudents(): Promise<Student[]> {
  const response = await apiClient.get<Student[]>('/api/students');
  return response.data;
}

export async function createStudent(data: CreateStudentRequest): Promise<Student> {
  const response = await apiClient.post<Student>('/api/students', data);
  return response.data;
}