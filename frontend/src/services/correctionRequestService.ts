import apiClient from './apiClient';
import type { CorrectionRequest } from '../types/correctionRequest';

export async function getPendingCorrectionRequests(): Promise<CorrectionRequest[]> {
  const response = await apiClient.get<CorrectionRequest[]>('/api/correction-requests/pending');
  return response.data;
}

export async function approveCorrectionRequest(id: number, notes: string): Promise<CorrectionRequest> {
  const response = await apiClient.post<CorrectionRequest>(`/api/correction-requests/${id}/approve`, { notes });
  return response.data;
}

export async function rejectCorrectionRequest(id: number, notes: string): Promise<CorrectionRequest> {
  const response = await apiClient.post<CorrectionRequest>(`/api/correction-requests/${id}/reject`, { notes });
  return response.data;
}