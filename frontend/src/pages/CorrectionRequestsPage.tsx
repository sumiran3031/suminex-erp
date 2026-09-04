import { useEffect, useState } from 'react';
import {
  getPendingCorrectionRequests, approveCorrectionRequest, rejectCorrectionRequest,
} from '../services/correctionRequestService';
import type { CorrectionRequest } from '../types/correctionRequest';

export default function CorrectionRequestsPage() {
  const [requests, setRequests] = useState<CorrectionRequest[]>([]);
  const [notes, setNotes] = useState<Record<number, string>>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    try {
      const data = await getPendingCorrectionRequests();
      setRequests(data);
    } catch {
      setError('Failed to load correction requests.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function handleApprove(id: number) {
    await approveCorrectionRequest(id, notes[id] ?? '');
    await load();
  }

  async function handleReject(id: number) {
    await rejectCorrectionRequest(id, notes[id] ?? '');
    await load();
  }

  return (
    <div>
      <h1>Correction Requests</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && requests.map((r) => (
        <div key={r.id} style={{ padding: 16, marginBottom: 12, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <p><strong>{r.studentName}</strong> — {r.subjectName}</p>
          <p>Requested by: {r.requestedByEmail}</p>
          <p>Reason: {r.reason}</p>
          <p>Current total: {r.currentTotal} → Proposed: {r.proposedInternalMarks + r.proposedExternalMarks + r.proposedPracticalMarks}
            {' '}(Internal: {r.proposedInternalMarks}, External: {r.proposedExternalMarks}, Practical: {r.proposedPracticalMarks})
          </p>
          <input
            placeholder="Review notes"
            value={notes[r.id] ?? ''}
            onChange={(e) => setNotes({ ...notes, [r.id]: e.target.value })}
            style={{ width: '100%', marginBottom: 8 }}
          />
          <button onClick={() => handleApprove(r.id)} style={{ marginRight: 8 }}>Approve</button>
          <button onClick={() => handleReject(r.id)}>Reject</button>
        </div>
      ))}
      {!loading && !error && requests.length === 0 && (
        <p style={{ color: '#64748b' }}>No pending correction requests.</p>
      )}
    </div>
  );
}