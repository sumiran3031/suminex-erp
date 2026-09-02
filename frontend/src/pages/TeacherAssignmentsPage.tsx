import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import { getAllSubjectOfferings } from '../services/timetableService';
import { getAssignmentsByOffering, createAssignment, getSubmissions } from '../services/assignmentService';
import type { SubjectOffering } from '../types/timetable';
import type { Assignment, AssignmentSubmission } from '../types/assignment';

export default function TeacherAssignmentsPage() {
  const [offerings, setOfferings] = useState<SubjectOffering[]>([]);
  const [selectedOfferingId, setSelectedOfferingId] = useState<number | null>(null);
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [submissionsByAssignment, setSubmissionsByAssignment] = useState<Record<number, AssignmentSubmission[]>>({});

  const [showForm, setShowForm] = useState(false);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [file, setFile] = useState<File | null>(null);
  const [formError, setFormError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function loadAssignments(offeringId: number) {
    const data = await getAssignmentsByOffering(offeringId);
    setAssignments(data);
  }

  useEffect(() => {
    getAllSubjectOfferings().then((data) => {
      setOfferings(data);
      if (data.length > 0) setSelectedOfferingId(data[0].id);
    });
  }, []);

  useEffect(() => {
    if (selectedOfferingId !== null) loadAssignments(selectedOfferingId);
  }, [selectedOfferingId]);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setSubmitting(true);

    const formData = new FormData();
    formData.append('subjectOfferingId', String(selectedOfferingId));
    formData.append('title', title);
    formData.append('description', description);
    formData.append('dueDate', dueDate);
    if (file) formData.append('file', file);

    try {
      await createAssignment(formData);
      setTitle(''); setDescription(''); setDueDate(''); setFile(null);
      setShowForm(false);
      if (selectedOfferingId !== null) await loadAssignments(selectedOfferingId);
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setFormError(err.response.data.message);
      } else {
        setFormError('Failed to create assignment.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  async function toggleSubmissions(assignmentId: number) {
    if (submissionsByAssignment[assignmentId]) {
      const copy = { ...submissionsByAssignment };
      delete copy[assignmentId];
      setSubmissionsByAssignment(copy);
      return;
    }
    const data = await getSubmissions(assignmentId);
    setSubmissionsByAssignment({ ...submissionsByAssignment, [assignmentId]: data });
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Assignments</h1>
        <button onClick={() => setShowForm(!showForm)}>{showForm ? 'Cancel' : '+ New Assignment'}</button>
      </div>

      <div style={{ margin: '16px 0' }}>
        <label>Subject Offering: </label>
        <select value={selectedOfferingId ?? ''} onChange={(e) => setSelectedOfferingId(Number(e.target.value))}>
          {offerings.map((o) => (
            <option key={o.id} value={o.id}>{o.subjectName} — Div {o.divisionName}</option>
          ))}
        </select>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <input placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)} required style={{ display: 'block', marginBottom: 8, width: '100%' }} />
          <textarea placeholder="Description" value={description} onChange={(e) => setDescription(e.target.value)} style={{ display: 'block', marginBottom: 8, width: '100%' }} />
          <input type="datetime-local" value={dueDate} onChange={(e) => setDueDate(e.target.value)} required style={{ display: 'block', marginBottom: 8 }} />
          <input type="file" onChange={(e) => setFile(e.target.files?.[0] ?? null)} style={{ display: 'block', marginBottom: 8 }} />
          {formError && <p style={{ color: 'red' }}>{formError}</p>}
          <button type="submit" disabled={submitting}>{submitting ? 'Creating...' : 'Create Assignment'}</button>
        </form>
      )}

      {assignments.map((a) => (
        <div key={a.id} style={{ padding: 12, marginBottom: 8, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <strong>{a.title}</strong> — Due: {new Date(a.dueDate).toLocaleString()}
          <p>{a.description}</p>
          <button onClick={() => toggleSubmissions(a.id)}>
            {submissionsByAssignment[a.id] ? 'Hide' : 'View'} Submissions
          </button>
          {submissionsByAssignment[a.id] && (
            <ul>
              {submissionsByAssignment[a.id].map((s) => (
                <li key={s.id}>{s.studentName} — submitted {new Date(s.submittedAt).toLocaleString()}</li>
              ))}
              {submissionsByAssignment[a.id].length === 0 && <li>No submissions yet.</li>}
            </ul>
          )}
        </div>
      ))}
      {assignments.length === 0 && <p style={{ color: '#64748b' }}>No assignments for this subject offering yet.</p>}
    </div>
  );
}