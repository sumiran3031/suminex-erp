import { useEffect, useState } from 'react';
import { getAuditLogs } from '../services/auditLogService';
import type { AuditLog } from '../types/auditLog';

export default function AuditLogsPage() {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [filter, setFilter] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function load(entityType?: string) {
    setLoading(true);
    setError(null);
    try {
      const data = await getAuditLogs(entityType || undefined);
      setLogs(data);
    } catch {
      setError('Failed to load audit logs.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  return (
    <div>
      <h1>Audit Logs</h1>
      <div style={{ margin: '16px 0' }}>
        <input
          placeholder="Filter by entity type (e.g. MarksEntry, User)"
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
          style={{ marginRight: 8 }}
        />
        <button onClick={() => load(filter)}>Filter</button>
        <button onClick={() => { setFilter(''); load(); }} style={{ marginLeft: 8 }}>Clear</button>
      </div>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '2px solid #e2e8f0' }}>
              <th style={{ padding: 8 }}>Time</th>
              <th style={{ padding: 8 }}>Actor</th>
              <th style={{ padding: 8 }}>Action</th>
              <th style={{ padding: 8 }}>Entity</th>
              <th style={{ padding: 8 }}>Old</th>
              <th style={{ padding: 8 }}>New</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((log) => (
              <tr key={log.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: 8, fontSize: 12 }}>{new Date(log.createdAt).toLocaleString()}</td>
                <td style={{ padding: 8 }}>{log.actorEmail}</td>
                <td style={{ padding: 8 }}>{log.action}</td>
                <td style={{ padding: 8 }}>{log.entityType} #{log.entityId}</td>
                <td style={{ padding: 8, fontSize: 12, maxWidth: 200 }}>{log.oldValue ?? '-'}</td>
                <td style={{ padding: 8, fontSize: 12, maxWidth: 200 }}>{log.newValue ?? '-'}</td>
              </tr>
            ))}
            {logs.length === 0 && (
              <tr><td colSpan={6} style={{ padding: 16, textAlign: 'center', color: '#64748b' }}>No logs found.</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}