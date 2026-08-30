import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getNavItemsForRole } from '../config/navigation';

export default function Sidebar() {
  const { user } = useAuth();

  if (!user) return null;

  const navItems = getNavItemsForRole(user.role);

  return (
    <aside
      style={{
        width: 220,
        minHeight: '100vh',
        backgroundColor: '#1e293b',
        color: '#fff',
        padding: '16px 0',
      }}
    >
      <div style={{ padding: '0 16px 24px', fontSize: 18, fontWeight: 'bold' }}>
        SumiNex ERP
      </div>
      <nav>
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            style={({ isActive }) => ({
              display: 'block',
              padding: '10px 16px',
              color: '#fff',
              textDecoration: 'none',
              backgroundColor: isActive ? '#334155' : 'transparent',
            })}
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
