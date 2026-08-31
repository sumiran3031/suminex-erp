import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import {
  getAllSubjectOfferings, getAllRooms, getAllTimeSlots,
  getTimetableByDivision, createTimetableEntry,
} from '../services/timetableService';
import type { SubjectOffering, Room, TimeSlot, TimetableEntry, DayOfWeek } from '../types/timetable';

const DAYS: DayOfWeek[] = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];

export default function TimetablePage() {
  const [offerings, setOfferings] = useState<SubjectOffering[]>([]);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [timeSlots, setTimeSlots] = useState<TimeSlot[]>([]);
  const [entries, setEntries] = useState<TimetableEntry[]>([]);

  const [selectedDivisionId, setSelectedDivisionId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    subjectOfferingId: '', dayOfWeek: 'MONDAY' as DayOfWeek, timeSlotId: '', roomId: '',
  });
  const [formError, setFormError] = useState<string | null>(null);
  const [formSuccess, setFormSuccess] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function loadDropdownData() {
    setLoading(true);
    setError(null);
    try {
      const [offeringData, roomData, timeSlotData] = await Promise.all([
        getAllSubjectOfferings(),
        getAllRooms(),
        getAllTimeSlots(),
      ]);
      setOfferings(offeringData);
      setRooms(roomData);
      setTimeSlots(timeSlotData);

      // Default to viewing the first division found among the offerings, if any.
      if (offeringData.length > 0 && selectedDivisionId === null) {
        setSelectedDivisionId(offeringData[0].divisionId);
      }
    } catch (err) {
      setError('Failed to load timetable data.');
    } finally {
      setLoading(false);
    }
  }

  async function loadEntriesForDivision(divisionId: number) {
    try {
      const data = await getTimetableByDivision(divisionId);
      setEntries(data);
    } catch (err) {
      setError('Failed to load timetable entries.');
    }
  }

  useEffect(() => {
    loadDropdownData();
  }, []);

  useEffect(() => {
    if (selectedDivisionId !== null) {
      loadEntriesForDivision(selectedDivisionId);
    }
  }, [selectedDivisionId]);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setFormSuccess(null);
    setSubmitting(true);

    try {
      await createTimetableEntry({
        subjectOfferingId: Number(formData.subjectOfferingId),
        dayOfWeek: formData.dayOfWeek,
        timeSlotId: Number(formData.timeSlotId),
        roomId: Number(formData.roomId),
      });
      setFormSuccess('Timetable entry created successfully.');
      setFormData({ subjectOfferingId: '', dayOfWeek: 'MONDAY', timeSlotId: '', roomId: '' });
      if (selectedDivisionId !== null) {
        await loadEntriesForDivision(selectedDivisionId);
      }
    } catch (err) {
      // This is where Day 16's conflict detection messages surface directly to the user.
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setFormError(err.response.data.message);
      } else {
        setFormError('Failed to create timetable entry.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  const uniqueDivisions = Array.from(
    new Map(offerings.map((o) => [o.divisionId, o.divisionName])).entries()
  );

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Timetable</h1>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Timetable Entry'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <select
              value={formData.subjectOfferingId}
              onChange={(e) => setFormData({ ...formData, subjectOfferingId: e.target.value })}
              required
            >
              <option value="">Select Subject Offering</option>
              {offerings.map((o) => (
                <option key={o.id} value={o.id}>
                  {o.subjectName} — {o.teacherName} — Div {o.divisionName}
                </option>
              ))}
            </select>
            <select
              value={formData.dayOfWeek}
              onChange={(e) => setFormData({ ...formData, dayOfWeek: e.target.value as DayOfWeek })}
              required
            >
              {DAYS.map((d) => <option key={d} value={d}>{d}</option>)}
            </select>
            <select
              value={formData.timeSlotId}
              onChange={(e) => setFormData({ ...formData, timeSlotId: e.target.value })}
              required
            >
              <option value="">Select Time Slot</option>
              {timeSlots.map((t) => (
                <option key={t.id} value={t.id}>{t.startTime} - {t.endTime}</option>
              ))}
            </select>
            <select
              value={formData.roomId}
              onChange={(e) => setFormData({ ...formData, roomId: e.target.value })}
              required
            >
              <option value="">Select Room</option>
              {rooms.map((r) => (
                <option key={r.id} value={r.id}>{r.name}</option>
              ))}
            </select>
          </div>
          {formError && <p style={{ color: 'red' }}>{formError}</p>}
          {formSuccess && <p style={{ color: 'green' }}>{formSuccess}</p>}
          <button type="submit" disabled={submitting} style={{ marginTop: 12 }}>
            {submitting ? 'Creating...' : 'Create Entry'}
          </button>
        </form>
      )}

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <>
          <div style={{ margin: '16px 0' }}>
            <label>View division: </label>
            <select
              value={selectedDivisionId ?? ''}
              onChange={(e) => setSelectedDivisionId(Number(e.target.value))}
            >
              {uniqueDivisions.map(([id, name]) => (
                <option key={id} value={id}>{name}</option>
              ))}
            </select>
          </div>

          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
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
              {entries.map((entry) => (
                <tr key={entry.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: 8 }}>{entry.dayOfWeek}</td>
                  <td style={{ padding: 8 }}>{entry.startTime} - {entry.endTime}</td>
                  <td style={{ padding: 8 }}>{entry.subjectName}</td>
                  <td style={{ padding: 8 }}>{entry.teacherName}</td>
                  <td style={{ padding: 8 }}>{entry.roomName}</td>
                </tr>
              ))}
              {entries.length === 0 && (
                <tr>
                  <td colSpan={5} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>
                    No timetable entries for this division yet.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}