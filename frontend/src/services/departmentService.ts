import apiClient from './apiClient';
import type { Department, CreateDepartmentRequest } from '../types/department';

export async function getAllDepartments(): Promise<Department[]> {
  const response = await apiClient.get<Department[]>('/api/departments');
  return response.data;
}

export async function createDepartment(data: CreateDepartmentRequest): Promise<Department> {
  const response = await apiClient.post<Department>('/api/departments', data);
  return response.data;
}