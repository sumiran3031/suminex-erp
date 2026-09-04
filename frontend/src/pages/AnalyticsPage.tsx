import { useEffect, useState } from 'react';
import { getAllSubjectOfferings } from '../services/timetableService';
import { getDivisionAttendance, getSubjectResultAnalytics } from '../services/analyticsService';
import type { SubjectOffering } from '../types/timetable';
import type { AttendanceAnalytics, ResultAnalytics } from '../types/analytics';

export default function AnalyticsPage() {
  const [offerings, setOfferings] = useState<SubjectOffering[]>([]);
  const [selectedOfferingId, setSelectedOfferingId] = useState<number | null>(null);
  const [attendance, setAttendance] = useState<AttendanceAnalytics[]>([]);
  const [results, setResults] = useState<ResultAnalytics | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getAllSubjectOfferings()
      .then((data) => {
        setOfferings(data);
        if (data.length > 0) setSelectedOfferingId(data[0].id);
      })
      .catch(() => setError('Failed to load subject offerings.'))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (selectedOfferingId === null) return;
    const offering = offerings.find((o) => o.id === selectedOfferingId);
    if (!offering) return;

    getDivisionAttendance(offering.divisionId).then(setAttendance).catch(() => setAttendance([]));
    getSubjectResultAnalytics(selectedOfferingId).then(setResults).catch(() => setResults(null));
  }, [selectedOfferingId, offerings]);

  return (
    <div>
      <h1>Analytics</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && (
        <>
          <div style={{ margin: '16px 0' }}>
            <label>Subject Offering: </label>
            <select value={selectedOfferingId ?? ''} onChange={(e) => setSelectedOfferingId(Number(e.target.value))}>
              {offerings.map((o) => (
                <option key={o.id} value={o.id}>{o.subjectName} — Div {o.divisionName}</option>
              ))}
            </select>
          </div>

          <h2>Attendance — Division</h2>
          <table style={{ width: '100%', borderCollapse: 'collapse', marginBottom: 24 }}>
            <thead>
              <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
                <th style={{ padding: 8 }}>Student</th>
                <th style={{ padding: 8 }}>Sessions</th>
                <th style={{ padding: 8 }}>Present</th>
                <th style={{ padding: 8 }}>%</th>
              </tr>
            </thead>
            <tbody>
              {attendance.map((a) => (
                <tr key={a.studentId} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: 8 }}>{a.studentName}</td>
                  <td style={{ padding: 8 }}>{a.totalSessions}</td>
                  <td style={{ padding: 8 }}>{a.presentCount}</td>
                  <td style={{ padding: 8, color: a.attendancePercentage < 75 ? 'red' : 'green' }}>
                    {a.attendancePercentage}%
                  </td>
                </tr>
              ))}
              {attendance.length === 0 && (
                <tr><td colSpan={4} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>No data yet.</td></tr>
              )}
            </tbody>
          </table>

          <h2>Results — {results?.subjectName ?? 'Subject'}</h2>
          {results && (
            <div style={{ padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
              <p>Total Students: {results.totalStudents}</p>
              <p>Class Average: {results.classAverage}</p>
              <p>Pass %: {results.passPercentage}%</p>
              <p>Grade Distribution:</p>
              <ul>
                {Object.entries(results.gradeDistribution).map(([grade, count]) => (
                  <li key={grade}>{grade}: {count}</li>
                ))}
              </ul>
            </div>
          )}
        </>
      )}
    </div>
  );
}