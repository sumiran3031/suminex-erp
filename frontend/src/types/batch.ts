export interface Batch {
  id: number;
  divisionId: number;
  divisionName: string;
  batchName: string;
}

export interface CreateBatchRequest {
  divisionId: number;
  batchName: string;
}