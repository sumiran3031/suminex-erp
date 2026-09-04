import apiClient from './apiClient';
import type { Assignment, AssignmentSubmission } from '../types/assignment';

export async function getAssignmentsByOffering(subjectOfferingId: number): Promise<Assignment[]> {
  const response = await apiClient.get<Assignment[]>(`/api/assignments/by-offering/${subjectOfferingId}`);
  return response.data;
}

export async function getMyAssignments(): Promise<Assignment[]> {
  const response = await apiClient.get<Assignment[]>('/api/assignments/my-assignments');
  return response.data;
}

export async function hasSubmitted(assignmentId: number): Promise<boolean> {
  const response = await apiClient.get<{ submitted: boolean }>(`/api/assignments/${assignmentId}/has-submitted`);
  return response.data.submitted;
}

export async function submitAssignment(assignmentId: number, file: File) {
  const formData = new FormData();
  formData.append('file', file);
  const response = await apiClient.post(`/api/assignments/${assignmentId}/submit`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
}

export async function createAssignment(formData: FormData): Promise<Assignment> {
  const response = await apiClient.post<Assignment>('/api/assignments', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
}

export async function getSubmissions(assignmentId: number): Promise<AssignmentSubmission[]> {
  const response = await apiClient.get<AssignmentSubmission[]>(`/api/assignments/${assignmentId}/submissions`);
  return response.data;
}