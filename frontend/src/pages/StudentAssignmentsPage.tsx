import { useEffect, useState } from 'react';
import axios from 'axios';
import { getMyAssignments, hasSubmitted, submitAssignment } from '../services/assignmentService';
import type { Assignment } from '../types/assignment';

export default function StudentAssignmentsPage() {
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [submittedMap, setSubmittedMap] = useState<Record<number, boolean>>({});
  const [files, setFiles] = useState<Record<number, File | null>>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [messageByAssignment, setMessageByAssignment] = useState<Record<number, string>>({});

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const data = await getMyAssignments();
      setAssignments(data);

      const statuses: Record<number, boolean> = {};
      for (const a of data) {
        statuses[a.id] = await hasSubmitted(a.id);
      }
      setSubmittedMap(statuses);
    } catch {
      setError('Failed to load assignments.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function handleSubmit(assignmentId: number) {
    const file = files[assignmentId];
    if (!file) {
      setMessageByAssignment({ ...messageByAssignment, [assignmentId]: 'Please select a file first.' });
      return;
    }

    try {
      await submitAssignment(assignmentId, file);
      setMessageByAssignment({ ...messageByAssignment, [assignmentId]: 'Submitted successfully!' });
      setSubmittedMap({ ...submittedMap, [assignmentId]: true });
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setMessageByAssignment({ ...messageByAssignment, [assignmentId]: err.response.data.message });
      } else {
        setMessageByAssignment({ ...messageByAssignment, [assignmentId]: 'Failed to submit.' });
      }
    }
  }

  return (
    <div>
      <h1>Assignments</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && assignments.map((a) => (
        <div key={a.id} style={{ padding: 16, marginBottom: 12, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <strong>{a.title}</strong> — {a.subjectName}
          <p>Due: {new Date(a.dueDate).toLocaleString()}</p>
          <p>{a.description}</p>

          {submittedMap[a.id] ? (
            <p style={{ color: 'green' }}>✓ Already submitted</p>
          ) : (
            <div>
              <input
                type="file"
                onChange={(e) => setFiles({ ...files, [a.id]: e.target.files?.[0] ?? null })}
              />
              <button onClick={() => handleSubmit(a.id)} style={{ marginLeft: 8 }}>Submit</button>
            </div>
          )}
          {messageByAssignment[a.id] && (
            <p style={{ color: messageByAssignment[a.id].includes('success') ? 'green' : 'red' }}>
              {messageByAssignment[a.id]}
            </p>
          )}
        </div>
      ))}
      {!loading && !error && assignments.length === 0 && (
        <p style={{ color: '#64748b' }}>No assignments yet.</p>
      )}
    </div>
  );
}