import type { Role } from '../types/auth';

export interface NavItem {
  label: string;
  path: string;
}

// Central definition of which nav items each role can see.
// Adding a new page later means adding one line here, not touching the Sidebar.
const navConfig: Record<Role, NavItem[]> = {
  SUPER_ADMIN: [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'Departments', path: '/departments' },
    { label: 'Users', path: '/users' },
    { label: 'Audit Logs', path: '/audit-logs' },
  ],
  ADMIN: [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'Students', path: '/students' },
    { label: 'Teachers', path: '/teachers' },
    { label: 'Departments', path: '/departments' },
    { label: 'Timetable', path: '/timetable' },
    { label: 'Results', path: '/results' },
  ],
  HOD: [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'My Department', path: '/my-department' },
    { label: 'Correction Requests', path: '/correction-requests' },
  ],
  TEACHER: [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'My Timetable', path: '/my-timetable' },
    { label: 'Attendance', path: '/attendance' },
    { label: 'Marks Entry', path: '/marks-entry' },
    { label: 'Assignments', path: '/assignments' },
  ],
  STAFF: [
    { label: 'Dashboard', path: '/dashboard' },
  ],
  STUDENT: [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'My Timetable', path: '/my-timetable' },
    { label: 'My Attendance', path: '/my-attendance' },
    { label: 'My Results', path: '/my-results' },
    { label: 'Assignments', path: '/assignments' },
    { label: 'Notifications', path: '/notifications' },
  ],
};

export function getNavItemsForRole(role: Role): NavItem[] {
  return navConfig[role] ?? [];
}