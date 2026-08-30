export interface Teacher {
  id: number;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  employeeCode: string;
  designation: string | null;
  departmentId: number | null;
  departmentName: string | null;
}

export interface CreateTeacherRequest {
  email: string;
  phone?: string;
  password: string;
  firstName: string;
  lastName: string;
  employeeCode: string;
  designation?: string;
  departmentId: number;
}