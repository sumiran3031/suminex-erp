import { useEffect, useState } from 'react';
import axios from 'axios';
import { getAllSubjectOfferings } from '../services/timetableService';
import {
  getSessionsByOffering, createTeachingSession, getRoster, markAttendance,
} from '../services/attendanceService';
import type { SubjectOffering } from '../types/timetable';
import type { Roster, TeachingSession, AttendanceStatus } from '../types/attendance';

export default function AttendanceMarkingPage() {
  const [offerings, setOfferings] = useState<SubjectOffering[]>([]);
  const [selectedOfferingId, setSelectedOfferingId] = useState<number | null>(null);
  const [sessions, setSessions] = useState<TeachingSession[]>([]);
  const [selectedSessionId, setSelectedSessionId] = useState<number | null>(null);
  const [roster, setRoster] = useState<Roster[]>([]);
  const [marks, setMarks] = useState<Record<number, AttendanceStatus>>({});

  const [newSessionDate, setNewSessionDate] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

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
    if (selectedOfferingId !== null) {
      getSessionsByOffering(selectedOfferingId).then(setSessions).catch(() => {});
      setSelectedSessionId(null);
      setRoster([]);
    }
  }, [selectedOfferingId]);

  useEffect(() => {
    if (selectedSessionId !== null) {
      getRoster(selectedSessionId).then((r) => {
        setRoster(r);
        const initial: Record<number, AttendanceStatus> = {};
        r.forEach((s) => { initial[s.studentId] = 'PRESENT'; });
        setMarks(initial);
      }).catch((err) => {
        if (axios.isAxiosError(err) && err.response?.data?.message) {
          setError(err.response.data.message);
        }
      });
    }
  }, [selectedSessionId]);

  async function handleCreateSession() {
    if (!newSessionDate || sessions.length === 0) return;
    try {
      const timetableId = sessions[0]?.timetableId;
      const created = await createTeachingSession(timetableId, newSessionDate);
      setSessions([...sessions, created]);
      setNewSessionDate('');
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setError(err.response.data.message);
      }
    }
  }

  async function handleSubmit() {
    setError(null);
    setSuccess(null);
    if (selectedSessionId === null) return;

    const entries = roster.map((s) => ({ studentId: s.studentId, status: marks[s.studentId] }));

    try {
      await markAttendance(selectedSessionId, entries);
      setSuccess('Attendance marked successfully.');
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setError(err.response.data.message);
      } else {
        setError('Failed to mark attendance.');
      }
    }
  }

  return (
    <div>
      <h1>Attendance</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {success && <p style={{ color: 'green' }}>{success}</p>}

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

          <div style={{ margin: '16px 0' }}>
            <label>Session: </label>
            <select
              value={selectedSessionId ?? ''}
              onChange={(e) => setSelectedSessionId(Number(e.target.value))}
            >
              <option value="">Select a session</option>
              {sessions.map((s) => (
                <option key={s.id} value={s.id}>{s.sessionDate}</option>
              ))}
            </select>
            <span style={{ marginLeft: 16 }}>
              <input type="date" value={newSessionDate} onChange={(e) => setNewSessionDate(e.target.value)} />
              <button onClick={handleCreateSession} style={{ marginLeft: 8 }}>+ New Session</button>
            </span>
          </div>

          {roster.length > 0 && (
            <>
              <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
                <thead>
                  <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
                    <th style={{ padding: 8 }}>Student</th>
                    <th style={{ padding: 8 }}>Roll Number</th>
                    <th style={{ padding: 8 }}>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {roster.map((s) => (
                    <tr key={s.studentId} style={{ borderBottom: '1px solid #f1f5f9' }}>
                      <td style={{ padding: 8 }}>{s.studentName}</td>
                      <td style={{ padding: 8 }}>{s.rollNumber}</td>
                      <td style={{ padding: 8 }}>
                        <select
                          value={marks[s.studentId]}
                          onChange={(e) => setMarks({ ...marks, [s.studentId]: e.target.value as AttendanceStatus })}
                        >
                          <option value="PRESENT">Present</option>
                          <option value="ABSENT">Absent</option>
                          <option value="LATE">Late</option>
                        </select>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <button onClick={handleSubmit} style={{ marginTop: 16 }}>Submit Attendance</button>
            </>
          )}
        </>
      )}
    </div>
  );
}