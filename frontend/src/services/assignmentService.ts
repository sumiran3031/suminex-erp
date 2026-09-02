import apiClient from './apiClient';
import type { Assignment, AssignmentSubmission } from '../types/assignment';

export async function getAssignmentsByOffering(subjectOfferingId: number): Promise<Assignment[]> {
  const response = await apiClient.get<Assignment[]>(`/api/assignments/by-offering/${subjectOfferingId}`);
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