import { useEffect, useState } from 'react';
import apiClient from '../services/apiClient';
import { getMyResults } from '../services/marksEntryService';
import type { MarksEntry } from '../types/marksEntry';

export default function MyResultsPage() {
  const [entries, setEntries] = useState<MarksEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [downloading, setDownloading] = useState(false);

  useEffect(() => {
    getMyResults()
      .then(setEntries)
      .catch(() => setError('Failed to load your results.'))
      .finally(() => setLoading(false));
  }, []);

  // Downloading requires knowing the student's own id and a semester id.
  // For today's scope, we use ids 1 and 1, matching our seeded test data —
  // a real implementation would resolve these from the logged-in user's own
  // active enrollment, similar to the other "my-*" endpoints built this week.
  async function handleDownload() {
    setDownloading(true);
    try {
      const response = await apiClient.get('/api/results/1/1/pdf', { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'result.pdf');
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch {
      setError('Failed to download result PDF.');
    } finally {
      setDownloading(false);
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>My Results</h1>
        <button onClick={handleDownload} disabled={downloading}>
          {downloading ? 'Downloading...' : 'Download Result PDF'}
        </button>
      </div>
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