import apiClient from './apiClient';
import type { Notification } from '../types/notification';

export async function getMyNotifications(): Promise<Notification[]> {
  const response = await apiClient.get<Notification[]>('/api/notifications');
  return response.data;
}

export async function markAsRead(id: number): Promise<Notification> {
  const response = await apiClient.patch<Notification>(`/api/notifications/${id}/read`);
  return response.data;
}