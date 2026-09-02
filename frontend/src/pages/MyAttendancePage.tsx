import { useEffect, useState } from 'react';
import { getMyAttendance } from '../services/attendanceService';
import type { MyAttendanceEntry } from '../services/attendanceService';

export default function MyAttendancePage() {
  const [entries, setEntries] = useState<MyAttendanceEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getMyAttendance()
      .then(setEntries)
      .catch(() => setError('Failed to load your attendance.'))
      .finally(() => setLoading(false));
  }, []);

  const total = entries.length;
  const present = entries.filter((e) => e.status === 'PRESENT').length;
  const percentage = total > 0 ? Math.round((present / total) * 10000) / 100 : 0;

  return (
    <div>
      <h1>My Attendance</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {!loading && !error && (
        <>
          <p><strong>Overall: {present}/{total} sessions ({percentage}%)</strong></p>
          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
            <thead>
              <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
                <th style={{ padding: 8 }}>#</th>
                <th style={{ padding: 8 }}>Status</th>
              </tr>
            </thead>
            <tbody>
              {entries.map((e, idx) => (
                <tr key={e.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: 8 }}>{idx + 1}</td>
                  <td style={{ padding: 8, color: e.status === 'PRESENT' ? 'green' : e.status === 'ABSENT' ? 'red' : 'orange' }}>
                    {e.status}
                  </td>
                </tr>
              ))}
              {entries.length === 0 && (
                <tr><td colSpan={2} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>No attendance records yet.</td></tr>
              )}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}