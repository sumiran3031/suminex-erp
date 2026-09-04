import apiClient from './apiClient';
import type { AuditLog } from '../types/auditLog';

export async function getAuditLogs(entityType?: string): Promise<AuditLog[]> {
  const params = entityType ? { entityType } : {};
  const response = await apiClient.get<AuditLog[]>('/api/audit-logs', { params });
  return response.data;
}