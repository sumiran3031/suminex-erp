import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import { getAllTeachers, createTeacher } from '../services/teacherService';
import { getAllDepartments } from '../services/departmentService';
import type { Teacher } from '../types/teacher';
import type { Department } from '../types/department';

export default function TeachersPage() {
  const [teachers, setTeachers] = useState<Teacher[]>([]);
  const [departments, setDepartments] = useState<Department[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    email: '', phone: '', password: '', firstName: '', lastName: '',
    employeeCode: '', designation: '', departmentId: '',
  });
  const [formError, setFormError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function loadData() {
    setLoading(true);
    setError(null);
    try {
      const [teacherData, departmentData] = await Promise.all([
        getAllTeachers(),
        getAllDepartments(),
      ]);
      setTeachers(teacherData);
      setDepartments(departmentData);
    } catch (err) {
      setError('Failed to load teachers.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setSubmitting(true);

    try {
      await createTeacher({
        ...formData,
        departmentId: Number(formData.departmentId),
      });
      setFormData({
        email: '', phone: '', password: '', firstName: '', lastName: '',
        employeeCode: '', designation: '', departmentId: '',
      });
      setShowForm(false);
      await loadData();
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data) {
        const data = err.response.data;
        if (data.details?.length > 0) {
          setFormError(data.details.join(', '));
        } else if (data.message) {
          setFormError(data.message);
        } else {
          setFormError('Failed to create teacher.');
        }
      } else {
        setFormError('Failed to create teacher.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Teachers</h1>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Teacher'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <input placeholder="Email" type="email" value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })} required />
            <input placeholder="Phone" value={formData.phone}
              onChange={(e) => setFormData({ ...formData, phone: e.target.value })} />
            <input placeholder="Password" type="password" value={formData.password}
              onChange={(e) => setFormData({ ...formData, password: e.target.value })} required />
            <input placeholder="First Name" value={formData.firstName}
              onChange={(e) => setFormData({ ...formData, firstName: e.target.value })} required />
            <input placeholder="Last Name" value={formData.lastName}
              onChange={(e) => setFormData({ ...formData, lastName: e.target.value })} required />
            <input placeholder="Employee Code" value={formData.employeeCode}
              onChange={(e) => setFormData({ ...formData, employeeCode: e.target.value })} required />
            <input placeholder="Designation" value={formData.designation}
              onChange={(e) => setFormData({ ...formData, designation: e.target.value })} />
            <select
              value={formData.departmentId}
              onChange={(e) => setFormData({ ...formData, departmentId: e.target.value })}
              required
            >
              <option value="">Select Department</option>
              {departments.map((d) => (
                <option key={d.id} value={d.id}>{d.name}</option>
              ))}
            </select>
          </div>
          {formError && <p style={{ color: 'red' }}>{formError}</p>}
          <button type="submit" disabled={submitting} style={{ marginTop: 12 }}>
            {submitting ? 'Creating...' : 'Create Teacher'}
          </button>
        </form>
      )}

      {loading && <p>Loading teachers...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Name</th>
              <th style={{ padding: 8 }}>Employee Code</th>
              <th style={{ padding: 8 }}>Designation</th>
              <th style={{ padding: 8 }}>Department</th>
              <th style={{ padding: 8 }}>Email</th>
            </tr>
          </thead>
          <tbody>
            {teachers.map((t) => (
              <tr key={t.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8 }}>{t.firstName} {t.lastName}</td>
                <td style={{ padding: 8 }}>{t.employeeCode}</td>
                <td style={{ padding: 8 }}>{t.designation ?? '-'}</td>
                <td style={{ padding: 8 }}>{t.departmentName ?? '-'}</td>
                <td style={{ padding: 8 }}>{t.email}</td>
              </tr>
            ))}
            {teachers.length === 0 && (
              <tr>
                <td colSpan={5} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>
                  No teachers yet.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}