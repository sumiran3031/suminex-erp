import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import { getAllSubjectOfferings } from '../services/timetableService';
import {
  getEligibleStudents, getMarksBySubjectOffering, createMarksEntry, updateMarksStatus,
} from '../services/marksEntryService';
import type { SubjectOffering } from '../types/timetable';
import type { EligibleStudent, MarksEntry, MarksEntryStatus } from '../types/marksEntry';
import { ALLOWED_TRANSITIONS } from '../types/marksEntry';

export default function MarksEntryPage() {
  const [offerings, setOfferings] = useState<SubjectOffering[]>([]);
  const [selectedOfferingId, setSelectedOfferingId] = useState<number | null>(null);
  const [students, setStudents] = useState<EligibleStudent[]>([]);
  const [entries, setEntries] = useState<MarksEntry[]>([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    studentId: '', internalMarks: '', externalMarks: '', practicalMarks: '',
  });
  const [formError, setFormError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const [statusError, setStatusError] = useState<string | null>(null);

  async function loadOfferings() {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllSubjectOfferings();
      setOfferings(data);
      if (data.length > 0 && selectedOfferingId === null) {
        setSelectedOfferingId(data[0].id);
      }
    } catch (err) {
      setError('Failed to load subject offerings.');
    } finally {
      setLoading(false);
    }
  }

  async function loadOfferingData(offeringId: number) {
    try {
      const [studentData, entryData] = await Promise.all([
        getEligibleStudents(offeringId),
        getMarksBySubjectOffering(offeringId),
      ]);
      setStudents(studentData);
      setEntries(entryData);
    } catch (err) {
      setError('Failed to load marks data.');
    }
  }

  useEffect(() => {
    loadOfferings();
  }, []);

  useEffect(() => {
    if (selectedOfferingId !== null) {
      loadOfferingData(selectedOfferingId);
    }
  }, [selectedOfferingId]);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setSubmitting(true);

    try {
      await createMarksEntry({
        subjectOfferingId: selectedOfferingId!,
        studentId: Number(formData.studentId),
        internalMarks: Number(formData.internalMarks),
        externalMarks: Number(formData.externalMarks),
        practicalMarks: Number(formData.practicalMarks),
      });
      setFormData({ studentId: '', internalMarks: '', externalMarks: '', practicalMarks: '' });
      setShowForm(false);
      if (selectedOfferingId !== null) {
        await loadOfferingData(selectedOfferingId);
      }
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data) {
        const data = err.response.data;
        if (data.details?.length > 0) setFormError(data.details.join(', '));
        else if (data.message) setFormError(data.message);
        else setFormError('Failed to create marks entry.');
      } else {
        setFormError('Failed to create marks entry.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  async function handleStatusChange(entryId: number, newStatus: MarksEntryStatus) {
    setStatusError(null);
    try {
      await updateMarksStatus(entryId, newStatus);
      if (selectedOfferingId !== null) {
        await loadOfferingData(selectedOfferingId);
      }
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setStatusError(err.response.data.message);
      } else {
        setStatusError('Failed to update status.');
      }
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Marks Entry</h1>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Marks Entry'}
        </button>
      </div>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <>
          <div style={{ margin: '16px 0' }}>
            <label>Subject Offering: </label>
            <select
              value={selectedOfferingId ?? ''}
              onChange={(e) => setSelectedOfferingId(Number(e.target.value))}
            >
              {offerings.map((o) => (
                <option key={o.id} value={o.id}>
                  {o.subjectName} — {o.teacherName} — Div {o.divisionName}
                </option>
              ))}
            </select>
          </div>

          {showForm && (
            <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                <select
                  value={formData.studentId}
                  onChange={(e) => setFormData({ ...formData, studentId: e.target.value })}
                  required
                >
                  <option value="">Select Student</option>
                  {students.map((s) => (
                    <option key={s.studentId} value={s.studentId}>{s.studentName} ({s.rollNumber})</option>
                  ))}
                </select>
                <input placeholder="Internal Marks" type="number" value={formData.internalMarks}
                  onChange={(e) => setFormData({ ...formData, internalMarks: e.target.value })} required />
                <input placeholder="External Marks" type="number" value={formData.externalMarks}
                  onChange={(e) => setFormData({ ...formData, externalMarks: e.target.value })} required />
                <input placeholder="Practical Marks" type="number" value={formData.practicalMarks}
                  onChange={(e) => setFormData({ ...formData, practicalMarks: e.target.value })} required />
              </div>
              {formError && <p style={{ color: 'red' }}>{formError}</p>}
              <button type="submit" disabled={submitting} style={{ marginTop: 12 }}>
                {submitting ? 'Saving...' : 'Save Marks Entry'}
              </button>
            </form>
          )}

          {statusError && <p style={{ color: 'red' }}>{statusError}</p>}

          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
            <thead>
              <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
                <th style={{ padding: 8 }}>Student</th>
                <th style={{ padding: 8 }}>Total</th>
                <th style={{ padding: 8 }}>Grade</th>
                <th style={{ padding: 8 }}>GP</th>
                <th style={{ padding: 8 }}>Pass</th>
                <th style={{ padding: 8 }}>Status</th>
                <th style={{ padding: 8 }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {entries.map((entry) => {
                const nextStatuses = ALLOWED_TRANSITIONS[entry.status];
                return (
                  <tr key={entry.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                    <td style={{ padding: 8 }}>{entry.studentName}</td>
                    <td style={{ padding: 8 }}>{entry.total}</td>
                    <td style={{ padding: 8 }}>{entry.grade}</td>
                    <td style={{ padding: 8 }}>{entry.gradePoint}</td>
                    <td style={{ padding: 8 }}>{entry.pass ? 'Yes' : 'No'}</td>
                    <td style={{ padding: 8 }}>{entry.status}</td>
                    <td style={{ padding: 8 }}>
                      {nextStatuses.length === 0 && <span style={{ color: '#64748b' }}>Final</span>}
                      {nextStatuses.map((next) => (
                        <button
                          key={next}
                          onClick={() => handleStatusChange(entry.id, next)}
                          style={{ marginRight: 6 }}
                        >
                          {next === 'DRAFT' ? 'Revert to Draft' : `Mark as ${next}`}
                        </button>
                      ))}
                    </td>
                  </tr>
                );
              })}
              {entries.length === 0 && (
                <tr>
                  <td colSpan={7} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>
                    No marks entries yet for this subject offering.
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