export type MarksEntryStatus = 'DRAFT' | 'SUBMITTED' | 'REVIEWED' | 'PUBLISHED';

export interface MarksEntry {
  id: number;
  studentId: number;
  studentName: string;
  subjectName: string;
  internalMarks: number;
  externalMarks: number;
  practicalMarks: number;
  total: number;
  grade: string;
  gradePoint: number;
  pass: boolean;
  status: MarksEntryStatus;
}

export interface EligibleStudent {
  studentId: number;
  studentName: string;
  rollNumber: string;
}

export interface CreateMarksEntryRequest {
  subjectOfferingId: number;
  studentId: number;
  internalMarks: number;
  externalMarks: number;
  practicalMarks: number;
}

// Mirrors Day 24's backend ALLOWED_TRANSITIONS map exactly — used to decide
// which status buttons to show/enable for a given entry.
export const ALLOWED_TRANSITIONS: Record<MarksEntryStatus, MarksEntryStatus[]> = {
  DRAFT: ['SUBMITTED'],
  SUBMITTED: ['REVIEWED', 'DRAFT'],
  REVIEWED: ['PUBLISHED', 'SUBMITTED'],
  PUBLISHED: [],
};