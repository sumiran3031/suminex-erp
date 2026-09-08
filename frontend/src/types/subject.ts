export type SubjectType = 'THEORY' | 'PRACTICAL';

export interface Subject {
  id: number;
  code: string;
  name: string;
  credits: number;
  subjectType: SubjectType;
  courseProgramId: number;
  courseProgramName: string;
  semesterId: number;
  semesterNumber: number;
}

export interface CreateSubjectRequest {
  code: string;
  name: string;
  credits: number;
  subjectType: SubjectType;
  courseProgramId: number;
  semesterId: number;
}