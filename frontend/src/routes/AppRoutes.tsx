import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import DashboardPage from '../pages/DashboardPage';
import StudentsPage from '../pages/StudentsPage';
import TeachersPage from '../pages/TeachersPage';
import DepartmentsPage from '../pages/DepartmentsPage';
import TimetablePage from '../pages/TimetablePage';
import MarksEntryPage from '../pages/MarksEntryPage';
import ProtectedRoute from './ProtectedRoute';
import AppLayout from '../layouts/AppLayout';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <AppLayout><DashboardPage /></AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/students"
        element={
          <ProtectedRoute>
            <AppLayout><StudentsPage /></AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/teachers"
        element={
          <ProtectedRoute>
            <AppLayout><TeachersPage /></AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/departments"
        element={
          <ProtectedRoute>
            <AppLayout><DepartmentsPage /></AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/timetable"
        element={
          <ProtectedRoute>
            <AppLayout><TimetablePage /></AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/marks-entry"
        element={
          <ProtectedRoute>
            <AppLayout><MarksEntryPage /></AppLayout>
          </ProtectedRoute>
        }
      />
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
