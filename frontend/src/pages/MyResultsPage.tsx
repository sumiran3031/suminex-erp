import { useEffect, useState } from 'react';
import { getMyResults } from '../services/marksEntryService';
import type { MarksEntry } from '../types/marksEntry';

export default function MyResultsPage() {
  const [entries, setEntries] = useState<MarksEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getMyResults()
      .then(setEntries)
      .catch(() => setError('Failed to load your results.'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h1>My Results</h1>
      <p style={{ color: '#64748b' }}>Only published results are shown here.</p>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Subject</th>
              <th style={{ padding: 8 }}>Total</th>
              <th style={{ padding: 8 }}>Grade</th>
              <th style={{ padding: 8 }}>GP</th>
              <th style={{ padding: 8 }}>Pass</th>
            </tr>
          </thead>
          <tbody>
            {entries.map((e) => (
              <tr key={e.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8 }}>{e.subjectName}</td>
                <td style={{ padding: 8 }}>{e.total}</td>
                <td style={{ padding: 8 }}>{e.grade}</td>
                <td style={{ padding: 8 }}>{e.gradePoint}</td>
                <td style={{ padding: 8 }}>{e.pass ? 'Yes' : 'No'}</td>
              </tr>
            ))}
            {entries.length === 0 && (
              <tr><td colSpan={5} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>No published results yet.</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}