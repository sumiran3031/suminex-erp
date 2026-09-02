import { useEffect, useState } from 'react';
import { getMyNotifications, markAsRead } from '../services/notificationService';
import type { Notification } from '../types/notification';

export default function NotificationsPage() {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    try {
      const data = await getMyNotifications();
      setNotifications(data);
    } catch {
      setError('Failed to load notifications.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function handleMarkRead(id: number) {
    await markAsRead(id);
    await load();
  }

  return (
    <div>
      <h1>Notifications</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {!loading && !error && (
        <div style={{ marginTop: 16 }}>
          {notifications.map((n) => (
            <div
              key={n.id}
              style={{
                padding: 12,
                marginBottom: 8,
                border: '1px solid #e2e8f0',
                borderRadius: 4,
                backgroundColor: n.read ? '#fff' : '#f0f9ff',
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <strong>{n.type.replace('_', ' ')}</strong>
                {!n.read && (
                  <button onClick={() => handleMarkRead(n.id)} style={{ fontSize: 12 }}>Mark as read</button>
                )}
              </div>
              <p style={{ margin: '4px 0' }}>{n.message}</p>
              <span style={{ fontSize: 12, color: '#64748b' }}>{new Date(n.createdAt).toLocaleString()}</span>
            </div>
          ))}
          {notifications.length === 0 && <p style={{ color: '#64748b' }}>No notifications yet.</p>}
        </div>
      )}
    </div>
  );
}