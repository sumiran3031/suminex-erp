import apiClient from './apiClient';
import type {
  MarksEntry, EligibleStudent, CreateMarksEntryRequest, MarksEntryStatus,
} from '../types/marksEntry';

export async function getEligibleStudents(subjectOfferingId: number): Promise<EligibleStudent[]> {
  const response = await apiClient.get<EligibleStudent[]>(`/api/marks-entries/eligible-students/${subjectOfferingId}`);
  return response.data;
}

export async function getMarksBySubjectOffering(subjectOfferingId: number): Promise<MarksEntry[]> {
  const response = await apiClient.get<MarksEntry[]>(`/api/marks-entries/by-offering/${subjectOfferingId}`);
  return response.data;
}

export async function createMarksEntry(data: CreateMarksEntryRequest): Promise<MarksEntry> {
  const response = await apiClient.post<MarksEntry>('/api/marks-entries', data);
  return response.data;
}

export async function updateMarksStatus(id: number, status: MarksEntryStatus): Promise<MarksEntry> {
  const response = await apiClient.patch<MarksEntry>(`/api/marks-entries/${id}/status`, { status });
  return response.data;
}