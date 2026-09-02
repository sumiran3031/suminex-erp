import { Routes, Route, Navigate } from 'react-router-dom';

import LoginPage from '../pages/LoginPage';
import DashboardPage from '../pages/DashboardPage';
import StudentsPage from '../pages/StudentsPage';
import TeachersPage from '../pages/TeachersPage';
import DepartmentsPage from '../pages/DepartmentsPage';
import TimetablePage from '../pages/TimetablePage';
import MarksEntryPage from '../pages/MarksEntryPage';

import MyTimetablePage from '../pages/MyTimetablePage';
import MyResultsPage from '../pages/MyResultsPage';
import MyAttendancePage from '../pages/MyAttendancePage';
import NotificationsPage from '../pages/NotificationsPage';

import ProtectedRoute from './ProtectedRoute';
import AppLayout from '../layouts/AppLayout';

export default function AppRoutes() {
  return (
    <Routes>

      {/* ==================== LOGIN ==================== */}

      <Route
        path="/login"
        element={<LoginPage />}
      />


      {/* ==================== DASHBOARD ==================== */}

      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <AppLayout>
              <DashboardPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== STUDENTS ==================== */}

      <Route
        path="/students"
        element={
          <ProtectedRoute>
            <AppLayout>
              <StudentsPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== TEACHERS ==================== */}

      <Route
        path="/teachers"
        element={
          <ProtectedRoute>
            <AppLayout>
              <TeachersPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== DEPARTMENTS ==================== */}

      <Route
        path="/departments"
        element={
          <ProtectedRoute>
            <AppLayout>
              <DepartmentsPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== TIMETABLE ==================== */}

      <Route
        path="/timetable"
        element={
          <ProtectedRoute>
            <AppLayout>
              <TimetablePage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== MARKS ENTRY ==================== */}

      <Route
        path="/marks-entry"
        element={
          <ProtectedRoute>
            <AppLayout>
              <MarksEntryPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ================================================== */}
      {/*                    STUDENT                         */}
      {/* ================================================== */}


      {/* ==================== MY TIMETABLE ==================== */}

      <Route
        path="/my-timetable"
        element={
          <ProtectedRoute>
            <AppLayout>
              <MyTimetablePage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== MY RESULTS ==================== */}

      <Route
        path="/my-results"
        element={
          <ProtectedRoute>
            <AppLayout>
              <MyResultsPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== MY ATTENDANCE ==================== */}

      <Route
        path="/my-attendance"
        element={
          <ProtectedRoute>
            <AppLayout>
              <MyAttendancePage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== NOTIFICATIONS ==================== */}

      <Route
        path="/notifications"
        element={
          <ProtectedRoute>
            <AppLayout>
              <NotificationsPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />


      {/* ==================== DEFAULT ==================== */}

      <Route
        path="/"
        element={
          <Navigate
            to="/dashboard"
            replace
          />
        }
      />

    </Routes>
  );
}