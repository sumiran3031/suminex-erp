export interface AuditLog {
  id: number;
  actorEmail: string;
  action: string;
  entityType: string;
  entityId: number;
  oldValue: string | null;
  newValue: string | null;
  createdAt: string;
}