import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <header style={{
        display: 'flex', justifyContent: 'space-between', alignItems: 'center',
        padding: '20px 40px', borderBottom: '1px solid var(--color-border)',
      }}>
        <span style={{ fontSize: 20, fontWeight: 700, color: 'var(--color-primary)' }}>SumiNex ERP</span>
        <Link to="/login">
          <button className="primary">Sign In</button>
        </Link>
      </header>

      <main style={{
        flex: 1, display: 'flex', flexDirection: 'column',
        alignItems: 'center', justifyContent: 'center', padding: 40, textAlign: 'center',
      }}>
        <h1 style={{ fontSize: 40, maxWidth: 640 }}>
          Integrated College Academic & ERP Management System
        </h1>
        <p style={{ maxWidth: 560, fontSize: 16, marginBottom: 32 }}>
          Manage students, faculty, timetables, attendance, marks, and results — all in one place.
          Built for academic institutions, designed for real workflows.
        </p>
        <Link to="/login">
          <button className="primary" style={{ padding: '12px 32px', fontSize: 16 }}>
            Get Started
          </button>
        </Link>

        <div style={{
          display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 24,
          marginTop: 64, maxWidth: 900,
        }}>
          <FeatureCard title="Academic Management" desc="Departments, courses, timetables, and conflict-free scheduling." />
          <FeatureCard title="Attendance & Marks" desc="Batch-wise attendance, grading, SGPA calculation, and result publishing." />
          <FeatureCard title="Role-Based Access" desc="Admins, HODs, teachers, and students each see exactly what they need." />
        </div>
      </main>

      <footer style={{ padding: 20, textAlign: 'center', color: 'var(--color-text-muted)', fontSize: 13 }}>
        SumiNex ERP — Built as a full-stack academic management system.
      </footer>
    </div>
  );
}

function FeatureCard({ title, desc }: { title: string; desc: string }) {
  return (
    <div className="card" style={{ textAlign: 'left' }}>
      <h3>{title}</h3>
      <p style={{ margin: 0, fontSize: 14 }}>{desc}</p>
    </div>
  );
}