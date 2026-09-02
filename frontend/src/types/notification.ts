export type NotificationType = 'NEW_ASSIGNMENT' | 'RESULT_PUBLISHED' | 'ATTENDANCE_SHORTAGE' | 'ANNOUNCEMENT' | 'TIMETABLE_CHANGE';

export interface Notification {
  id: number;
  type: NotificationType;
  message: string;
  read: boolean;
  createdAt: string;
}