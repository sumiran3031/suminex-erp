import { useAuth } from '../context/AuthContext';

export default function DashboardPage() {
  const { user, logout } = useAuth();

  return (
    <div style={{ maxWidth: 600, margin: '40px auto', fontFamily: 'sans-serif' }}>
      <h1>Dashboard</h1>
      <p>Welcome, {user?.email}</p>
      <p>Role: {user?.role}</p>
      <button onClick={logout}>Log out</button>
    </div>
  );
}
