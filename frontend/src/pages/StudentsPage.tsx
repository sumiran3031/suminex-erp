import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import { getAllStudents, createStudent } from '../services/studentService';
import type { Student } from '../types/student';

export default function StudentsPage() {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    phone: '',
    password: '',
    firstName: '',
    lastName: '',
    rollNumber: '',
    prn: '',
  });
  const [formError, setFormError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function loadStudents() {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllStudents();
      setStudents(data);
    } catch (err) {
      setError('Failed to load students.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadStudents();
  }, []);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setSubmitting(true);

    try {
      await createStudent(formData);
      setFormData({
        email: '', phone: '', password: '', firstName: '', lastName: '', rollNumber: '', prn: '',
      });
      setShowForm(false);
      await loadStudents();
    } catch (err) {
    if (axios.isAxiosError(err) && err.response?.data) {
    const data = err.response.data;
    if (data.details && Array.isArray(data.details) && data.details.length > 0) {
      setFormError(data.details.join(', '));
    } else if (data.message) {
      setFormError(data.message);
    } else {
      setFormError('Failed to create student.');
    }
  } else {
    setFormError('Failed to create student.');
  }
} finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Students</h1>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Student'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <input
              placeholder="Email"
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              required
            />
            <input
              placeholder="Phone"
              value={formData.phone}
              onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
            />
            <input
              placeholder="Password"
              type="password"
              value={formData.password}
              onChange={(e) => setFormData({ ...formData, password: e.target.value })}
              required
            />
            <input
              placeholder="First Name"
              value={formData.firstName}
              onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
              required
            />
            <input
              placeholder="Last Name"
              value={formData.lastName}
              onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
              required
            />
            <input
              placeholder="Roll Number"
              value={formData.rollNumber}
              onChange={(e) => setFormData({ ...formData, rollNumber: e.target.value })}
              required
            />
            <input
              placeholder="PRN (optional)"
              value={formData.prn}
              onChange={(e) => setFormData({ ...formData, prn: e.target.value })}
            />
          </div>
          {formError && <p style={{ color: 'red' }}>{formError}</p>}
          <button type="submit" disabled={submitting} style={{ marginTop: 12 }}>
            {submitting ? 'Creating...' : 'Create Student'}
          </button>
        </form>
      )}

      {loading && <p>Loading students...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Name</th>
              <th style={{ padding: 8 }}>Roll Number</th>
              <th style={{ padding: 8 }}>PRN</th>
              <th style={{ padding: 8 }}>Email</th>
            </tr>
          </thead>
          <tbody>
            {students.map((s) => (
              <tr key={s.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8 }}>{s.firstName} {s.lastName}</td>
                <td style={{ padding: 8 }}>{s.rollNumber}</td>
                <td style={{ padding: 8 }}>{s.prn ?? '-'}</td>
                <td style={{ padding: 8 }}>{s.email}</td>
              </tr>
            ))}
            {students.length === 0 && (
              <tr>
                <td colSpan={4} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>
                  No students yet.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}