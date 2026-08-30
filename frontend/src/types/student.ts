export interface Student {
  id: number;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  rollNumber: string;
  prn: string | null;
  dateOfBirth: string | null;
}

export interface CreateStudentRequest {
  email: string;
  phone?: string;
  password: string;
  firstName: string;
  lastName: string;
  rollNumber: string;
  prn?: string;
  dateOfBirth?: string;
}