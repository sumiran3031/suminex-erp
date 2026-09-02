import apiClient from './apiClient';
import type {
  SubjectOffering, Room, TimeSlot, TimetableEntry, CreateTimetableRequest,
} from '../types/timetable';

export async function getAllSubjectOfferings(): Promise<SubjectOffering[]> {
  const response = await apiClient.get<SubjectOffering[]>('/api/subject-offerings');
  return response.data;
}

export async function getAllRooms(): Promise<Room[]> {
  const response = await apiClient.get<Room[]>('/api/rooms');
  return response.data;
}

export async function getAllTimeSlots(): Promise<TimeSlot[]> {
  const response = await apiClient.get<TimeSlot[]>('/api/time-slots');
  return response.data;
}

export async function getTimetableByDivision(divisionId: number): Promise<TimetableEntry[]> {
  const response = await apiClient.get<TimetableEntry[]>(`/api/timetables/by-division/${divisionId}`);
  return response.data;
}

export async function createTimetableEntry(data: CreateTimetableRequest): Promise<TimetableEntry> {
  const response = await apiClient.post<TimetableEntry>('/api/timetables', data);
  return response.data;
}

export async function getMyTimetable(): Promise<TimetableEntry[]> {
  const response = await apiClient.get<TimetableEntry[]>('/api/timetables/my-timetable');
  return response.data;
}

export async function getTimetableByOffering(subjectOfferingId: number): Promise<TimetableEntry[]> {
  const response = await apiClient.get<TimetableEntry[]>(`/api/timetables/by-offering/${subjectOfferingId}`);
  return response.data;
}