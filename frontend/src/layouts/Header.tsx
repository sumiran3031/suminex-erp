import { useAuth } from '../context/AuthContext';

export default function Header() {
  const { user, logout } = useAuth();

  return (
    <header
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '12px 24px',
        borderBottom: '1px solid #e2e8f0',
        backgroundColor: '#fff',
      }}
    >
      <div />
      <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
        <span>
          {user?.email} <span style={{ color: '#64748b' }}>({user?.role})</span>
        </span>
        <button onClick={logout}>Log out</button>
      </div>
    </header>
  );
}
