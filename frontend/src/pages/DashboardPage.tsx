import { useAuth } from '../context/AuthContext';
import ProfilePhotoUpload from '../components/ProfilePhotoUpload';

export default function DashboardPage() {
  const { user } = useAuth();

  return (
    <div>
      <h1>Dashboard</h1>
      <p>Welcome back, {user?.email}.</p>
      <p>You are logged in as: <strong>{user?.role}</strong></p>
      <ProfilePhotoUpload />
    </div>
  );
}