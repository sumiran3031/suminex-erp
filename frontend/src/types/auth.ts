export type Role = 'SUPER_ADMIN' | 'ADMIN' | 'HOD' | 'TEACHER' | 'STAFF' | 'STUDENT';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  role: Role;
}

export interface AuthUser {
  email: string;
  role: Role;
}