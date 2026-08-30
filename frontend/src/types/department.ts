export interface Department {
  id: number;
  name: string;
  code: string;
}

export interface CreateDepartmentRequest {
  name: string;
  code: string;
}