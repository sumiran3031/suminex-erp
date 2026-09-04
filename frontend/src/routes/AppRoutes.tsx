import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import DashboardPage from '../pages/DashboardPage';
import StudentsPage from '../pages/StudentsPage';
import TeachersPage from '../pages/TeachersPage';
import DepartmentsPage from '../pages/DepartmentsPage';
import TimetablePage from '../pages/TimetablePage';
import MarksEntryPage from '../pages/MarksEntryPage';
import MyTimetablePage from '../pages/MyTimetablePage';
import MyAttendancePage from '../pages/MyAttendancePage';
import MyResultsPage from '../pages/MyResultsPage';
import NotificationsPage from '../pages/NotificationsPage';
import AttendanceMarkingPage from '../pages/AttendanceMarkingPage';
import TeacherAssignmentsPage from '../pages/TeacherAssignmentsPage';
import AnalyticsPage from '../pages/AnalyticsPage';
import AuditLogsPage from '../pages/AuditLogsPage';
import CorrectionRequestsPage from '../pages/CorrectionRequestsPage';
import ProtectedRoute from './ProtectedRoute';
import AppLayout from '../layouts/AppLayout';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/dashboard" element={<ProtectedRoute><AppLayout><DashboardPage /></AppLayout></ProtectedRoute>} />
      <Route path="/students" element={<ProtectedRoute><AppLayout><StudentsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/teachers" element={<ProtectedRoute><AppLayout><TeachersPage /></AppLayout></ProtectedRoute>} />
      <Route path="/departments" element={<ProtectedRoute><AppLayout><DepartmentsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/timetable" element={<ProtectedRoute><AppLayout><TimetablePage /></AppLayout></ProtectedRoute>} />
      <Route path="/marks-entry" element={<ProtectedRoute><AppLayout><MarksEntryPage /></AppLayout></ProtectedRoute>} />
      <Route path="/my-timetable" element={<ProtectedRoute><AppLayout><MyTimetablePage /></AppLayout></ProtectedRoute>} />
      <Route path="/my-attendance" element={<ProtectedRoute><AppLayout><MyAttendancePage /></AppLayout></ProtectedRoute>} />
      <Route path="/my-results" element={<ProtectedRoute><AppLayout><MyResultsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/notifications" element={<ProtectedRoute><AppLayout><NotificationsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/attendance" element={<ProtectedRoute><AppLayout><AttendanceMarkingPage /></AppLayout></ProtectedRoute>} />
      <Route path="/assignments" element={<ProtectedRoute><AppLayout><TeacherAssignmentsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/analytics" element={<ProtectedRoute><AppLayout><AnalyticsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/audit-logs" element={<ProtectedRoute><AppLayout><AuditLogsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/correction-requests" element={<ProtectedRoute><AppLayout><CorrectionRequestsPage /></AppLayout></ProtectedRoute>} />
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}