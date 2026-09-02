export interface Assignment {
  id: number;
  title: string;
  description: string | null;
  subjectName: string;
  dueDate: string;
  filePath: string | null;
}

export interface AssignmentSubmission {
  id: number;
  studentId: number;
  studentName: string;
  filePath: string;
  submittedAt: string;
}