import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import { getAllDepartments, createDepartment } from '../services/departmentService';
import type { Department } from '../types/department';

export default function DepartmentsPage() {
  const [departments, setDepartments] = useState<Department[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [name, setName] = useState('');
  const [code, setCode] = useState('');
  const [formError, setFormError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function loadDepartments() {
    setLoading(true);
    setError(null);
    try {
      const data = await getAllDepartments();
      setDepartments(data);
    } catch (err) {
      setError('Failed to load departments.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadDepartments();
  }, []);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setSubmitting(true);

    try {
      await createDepartment({ name, code });
      setName('');
      setCode('');
      setShowForm(false);
      await loadDepartments();
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data) {
        const data = err.response.data;
        if (data.details?.length > 0) {
          setFormError(data.details.join(', '));
        } else if (data.message) {
          setFormError(data.message);
        } else {
          setFormError('Failed to create department.');
        }
      } else {
        setFormError('Failed to create department.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Departments</h1>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Department'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <input
              placeholder="Department Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
            <input
              placeholder="Code (e.g. ENTC)"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              required
            />
          </div>
          {formError && <p style={{ color: 'red' }}>{formError}</p>}
          <button type="submit" disabled={submitting} style={{ marginTop: 12 }}>
            {submitting ? 'Creating...' : 'Create Department'}
          </button>
        </form>
      )}

      {loading && <p>Loading departments...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Name</th>
              <th style={{ padding: 8 }}>Code</th>
            </tr>
          </thead>
          <tbody>
            {departments.map((d) => (
              <tr key={d.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8 }}>{d.name}</td>
                <td style={{ padding: 8 }}>{d.code}</td>
              </tr>
            ))}
            {departments.length === 0 && (
              <tr>
                <td colSpan={2} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>
                  No departments yet.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}