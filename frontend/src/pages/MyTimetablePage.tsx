import { useEffect, useState } from 'react';
import { getMyTimetable } from '../services/timetableService';
import type { TimetableEntry } from '../types/timetable';

export default function MyTimetablePage() {
  const [entries, setEntries] = useState<TimetableEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getMyTimetable()
      .then(setEntries)
      .catch(() => setError('Failed to load your timetable.'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h1>My Timetable</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Day</th>
              <th style={{ padding: 8 }}>Time</th>
              <th style={{ padding: 8 }}>Subject</th>
              <th style={{ padding: 8 }}>Teacher</th>
              <th style={{ padding: 8 }}>Room</th>
            </tr>
          </thead>
          <tbody>
            {entries.map((e) => (
              <tr key={e.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8 }}>{e.dayOfWeek}</td>
                <td style={{ padding: 8 }}>{e.startTime} - {e.endTime}</td>
                <td style={{ padding: 8 }}>{e.subjectName}</td>
                <td style={{ padding: 8 }}>{e.teacherName}</td>
                <td style={{ padding: 8 }}>{e.roomName}</td>
              </tr>
            ))}
            {entries.length === 0 && (
              <tr><td colSpan={5} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>No timetable entries yet.</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}