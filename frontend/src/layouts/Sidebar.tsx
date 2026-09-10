import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getNavItemsForRole } from '../config/navigation';

export default function Sidebar() {
  const { user } = useAuth();
  if (!user) return null;

  const navItems = getNavItemsForRole(user.role);

  return (
    <aside style={{
      width: 240, minHeight: '100vh', backgroundColor: 'var(--color-sidebar)',
      color: '#fff', padding: '20px 0', flexShrink: 0,
    }}>
      <div style={{ padding: '0 20px 28px', fontSize: 20, fontWeight: 700 }}>
        SumiNex ERP
      </div>
      <nav>
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            style={({ isActive }) => ({
              display: 'block',
              padding: '10px 20px',
              margin: '2px 8px',
              borderRadius: 6,
              color: '#fff',
              textDecoration: 'none',
              fontSize: 14,
              fontWeight: isActive ? 600 : 400,
              backgroundColor: isActive ? 'var(--color-primary)' : 'transparent',
            })}
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}