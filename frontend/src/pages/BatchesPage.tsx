import { useEffect, useState, type FormEvent } from 'react';
import axios from 'axios';
import { getBatchesByDivision, createBatch } from '../services/batchService';
import type { Batch } from '../types/batch';

const DEFAULT_DIVISION_ID = 1;

export default function BatchesPage() {
  const [batches, setBatches] = useState<Batch[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [batchName, setBatchName] = useState('');
  const [formError, setFormError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const data = await getBatchesByDivision(DEFAULT_DIVISION_ID);
      setBatches(data);
    } catch {
      setError('Failed to load batches.');
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
      await createBatch({ divisionId: DEFAULT_DIVISION_ID, batchName });
      setBatchName('');
      setShowForm(false);
      await load();
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setFormError(err.response.data.message);
      } else {
        setFormError('Failed to create batch.');
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Batches</h1>
        <button onClick={() => setShowForm(!showForm)}>{showForm ? 'Cancel' : '+ Add Batch'}</button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ margin: '16px 0', padding: 16, border: '1px solid #e2e8f0', borderRadius: 4 }}>
          <input placeholder="Batch Name (e.g. A1)" value={batchName} onChange={(e) => setBatchName(e.target.value)} required />
          {formError && <p style={{ color: 'red' }}>{formError}</p>}
          <button type="submit" disabled={submitting} style={{ marginTop: 12, display: 'block' }}>
            {submitting ? 'Creating...' : 'Create Batch'}
          </button>
        </form>
      )}

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 16 }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Batch Name</th>
              <th style={{ padding: 8 }}>Division</th>
            </tr>
          </thead>
          <tbody>
            {batches.map((b) => (
              <tr key={b.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8 }}>{b.batchName}</td>
                <td style={{ padding: 8 }}>{b.divisionName}</td>
              </tr>
            ))}
            {batches.length === 0 && (
              <tr><td colSpan={2} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>No batches yet.</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}