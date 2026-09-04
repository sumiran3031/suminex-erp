import apiClient from './apiClient';
import type { ProfilePhotoResponse } from '../types/profile';

export async function uploadProfilePhoto(file: File): Promise<ProfilePhotoResponse> {
  const formData = new FormData();
  formData.append('file', file);
  const response = await apiClient.post<ProfilePhotoResponse>('/api/profile/photo', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
}

export function getFileUrl(path: string): string {
  const base = import.meta.env.VITE_API_BASE_URL;
  return `${base}/api/files?path=${encodeURIComponent(path)}`;
}