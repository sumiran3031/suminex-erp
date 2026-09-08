import apiClient from './apiClient';
import type { Batch, CreateBatchRequest } from '../types/batch';

export async function getBatchesByDivision(divisionId: number): Promise<Batch[]> {
  const response = await apiClient.get<Batch[]>(`/api/batches/by-division/${divisionId}`);
  return response.data;
}

export async function createBatch(data: CreateBatchRequest): Promise<Batch> {
  const response = await apiClient.post<Batch>('/api/batches', data);
  return response.data;
}