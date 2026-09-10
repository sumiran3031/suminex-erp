import { useAuth } from '../context/AuthContext';

export default function Header() {
  const { user, logout } = useAuth();

  return (
    <header style={{
      display: 'flex', justifyContent: 'flex-end', alignItems: 'center',
      padding: '14px 32px', borderBottom: '1px solid var(--color-border)',
      backgroundColor: 'var(--color-surface)',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
        <span style={{ fontSize: 14 }}>
          {user?.email} <span className="text-muted">({user?.role})</span>
        </span>
        <button onClick={logout}>Log out</button>
      </div>
    </header>
  );
}