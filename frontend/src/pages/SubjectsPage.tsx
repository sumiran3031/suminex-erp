import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import { getSubjectsBySemester, createSubject } from '../services/subjectService';
import type { Subject, SubjectType } from '../types/subject';

// Hardcoded to our known test data (courseProgramId 1, semesterId 1) — a fuller
// implementation would let the admin pick these via cascading dropdowns, similar
// to how Timetable lets you pick a division. Flagged as a simplification for today.
const DEFAULT_COURSE_PROGRAM_ID = 1;
const DEFAULT_SEMESTER_ID = 1;

export default function SubjectsPage() {
  const [subjects, setSubjects] = useState<Subject[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [code, setCode] = useState('');
  const [name, setName] = useState('');
  const [credits, setCredits] = useState('');
  const [subjectType, setSubjectType] = useState<SubjectType>('THEORY');
  const [formError, setFormError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const data = await getSubjectsBySemester(DEFAULT_SEMESTER_ID);
      setSubjects(data);
    } catch {
      setError('Failed to load subjects.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setSubmitting(true);

    try {
      await createSubject({
        code, name, credits: Number(credits), subjectType,
        courseProgramId: DEFAULT_COURSE_PROGRAM_ID,
        semesterId: DEFAULT_SEMESTER_ID,
      });
      setCode(''); setName(''); setCredits(''); setSubjectType('THEORY');
      setShowForm(false);
      await load();
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data) {
        const data = err.response.data;
        if (data.details?.length > 0) setFormError(data.details.join(', '));
        else if (data.message) setFormError(data.message);
        else setFormError('Failed to create subject.');
      } else {
        setFormError('Failed to create subject.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Subjects</h1>
        <button onClick={() => setShowForm(!showForm)}>{showForm ? 'Cancel' : '+ Add Subject'}</button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <input placeholder="Subject Code" value={code} onChange={(e) => setCode(e.target.value)} required />
            <input placeholder="Subject Name" value={name} onChange={(e) => setName(e.target.value)} required />
            <input placeholder="Credits" type="number" value={credits} onChange={(e) => setCredits(e.target.value)} required />
            <select value={subjectType} onChange={(e) => setSubjectType(e.target.value as SubjectType)}>
              <option value="THEORY">Theory</option>
              <option value="PRACTICAL">Practical</option>
            </select>
          </div>
          {formError && <p style={{ color: 'red' }}>{formError}</p>}
          <button type="submit" disabled={submitting} style={{ marginTop: 12 }}>
            {submitting ? 'Creating...' : 'Create Subject'}
          </button>
        </form>
      )}

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Code</th>
              <th style={{ padding: 8 }}>Name</th>
              <th style={{ padding: 8 }}>Credits</th>
              <th style={{ padding: 8 }}>Type</th>
              <th style={{ padding: 8 }}>Course</th>
            </tr>
          </thead>
          <tbody>
            {subjects.map((s) => (
              <tr key={s.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8 }}>{s.code}</td>
                <td style={{ padding: 8 }}>{s.name}</td>
                <td style={{ padding: 8 }}>{s.credits}</td>
                <td style={{ padding: 8 }}>{s.subjectType}</td>
                <td style={{ padding: 8 }}>{s.courseProgramName}</td>
              </tr>
            ))}
            {subjects.length === 0 && (
              <tr><td colSpan={5} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>No subjects yet.</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}