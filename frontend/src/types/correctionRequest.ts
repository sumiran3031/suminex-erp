export interface CorrectionRequest {
  id: number;
  marksEntryId: number;
  studentName: string;
  subjectName: string;
  requestedByEmail: string;
  reason: string;
  currentTotal: number;
  proposedInternalMarks: number;
  proposedExternalMarks: number;
  proposedPracticalMarks: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  reviewedByEmail: string | null;
  reviewNotes: string | null;
  createdAt: string;
  reviewedAt: string | null;
}