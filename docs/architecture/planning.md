# SumiNex ERP —  Planning & Architecture

## 1. Module List
1. Auth & User Management
2. Profile Management (Student/Teacher/Staff + Photo)
3. Academic Structure (Dept, Course, Year, Semester, Division)
4. Subject Management
5. Student & Batch Management
6. Enrollment / Academic Progression
7. Faculty & Teaching Assignment (SubjectOffering)
8. Timetable Management
9. Attendance Management (Theory + Practical)
10. Marks & Results (SGPA/CGPA)
11. Grading Configuration
12. Result Workflow & PDF Generation
13. Assignment Module
14. Analytics & Reports
15. Notifications
16. Audit Logging

## 2. User Roles
- SUPER_ADMIN
- ADMIN
- HOD
- TEACHER
- STAFF
- STUDENT

One `User` table backs all logins; role-specific data lives in linked entities (Student, Teacher, Staff).

## 3. Role–Permission Matrix (high-level)

| Capability | SUPER_ADMIN | ADMIN | HOD | TEACHER | STAFF | STUDENT |
|---|---|---|---|---|---|---|
| Manage admins/system config | Yes | No | No | No | No | No |
| Manage departments/courses/years | Yes | Yes | No | No | No | No |
| Manage subjects | Yes | Yes | View (dept) | No | No | No |
| Manage students (CRUD, batch) | Yes | Yes | View/manage (dept) | No | Scoped | No |
| Manage teachers/staff | Yes | Yes | View (dept) | No | No | No |
| Assign SubjectOffering | Yes | Yes | Yes (dept) | No | No | No |
| Manage timetable | Yes | Yes | Yes (dept) | View own | No | View own |
| Take attendance | No | No | View (dept) | Yes (assigned) | No | View own |
| Enter marks | No | No | View/approve (dept) | Yes (assigned) | No | View own (published) |
| Publish results | Yes | Yes | Yes (dept, approval step) | No | No | No |
| View analytics | Yes | Yes | Yes (dept) | Scoped (own subjects) | No | No |
| Manage assignments | No | No | View | Yes (assigned) | No | Submit only |
| View audit logs | Yes | Yes | Yes (dept, limited) | No | No | No |

STAFF permissions are configured per assigned responsibility — no default elevated access.

## 4. High-Level System Architecture

React SPA (TypeScript, Axios) communicates over HTTPS/JWT with a Spring Boot REST API (layered architecture), which uses JPA/Hibernate to talk to MySQL. A separate file storage layer (local initially, S3 later) handles profile photos, assignment files, and result PDFs. Cross-cutting concerns: Spring Security + JWT filter chain, global exception handler, audit-logging aspect, Swagger/OpenAPI docs.

## 5. Backend Architecture

- controller: REST endpoints only, thin, delegates to service
- service: business logic, transactions, validation, conflict detection
- repository: Spring Data JPA interfaces
- entity: JPA entities
- dto: request/response objects (never expose entities directly)
- mapper: entity <-> DTO conversion
- exception: custom exceptions + global handler
- security: JWT filter, UserDetailsService, SecurityConfig
- config: CORS, Swagger, file storage, app properties
- util: shared helpers
- audit: audit log entity/service/aspect

Key decisions:
- User/Student/Teacher/Staff use composition, not inheritance (one-to-one FK)
- SubjectOffering connects Subject + Teacher + AcademicYear + Semester + Division (+Batch)
- TeachingSession represents an actual scheduled/conducted class; Attendance always references a TeachingSession
- Enrollment history is append-only (StudentEnrollment records per semester)

## 6. Frontend Architecture

src/components, pages, layouts, services, hooks, context, routes, types, utils.
Routing uses a ProtectedRoute wrapper with role-based guarding; navigation menus generate from the logged-in user's role.

## 7. Database Entity List

Identity: User, Student, Teacher, Staff
Academic Structure: Department, CourseProgram, AcademicYear, Semester, Division, Subject, Batch
Enrollment: StudentEnrollment (history)
Teaching: SubjectOffering, Timetable, TimeSlot, Room
Attendance: TeachingSession, Attendance
Marks/Results: GradingScheme, GradeBand, MarksEntry, SemesterResult, ResultAuditLog
Assignments: Assignment, AssignmentSubmission
Notifications: Notification
Audit: AuditLog
Security/Recovery: PasswordResetToken / OtpVerification

## 8. Entity Relationships (core)

User 1-1 Student, Teacher, Staff
Department 1-* CourseProgram 1-* AcademicYear 1-* Semester 1-* Division 1-* Batch
StudentEnrollment links Student to AcademicYear/Semester/Division/Batch historically
SubjectOffering links Subject + Teacher + AcademicYear + Semester + Division + Batch
Timetable links SubjectOffering + TimeSlot + Room + DayOfWeek
TeachingSession links Timetable + Date
Attendance links TeachingSession + Student (unique constraint)
MarksEntry links SubjectOffering + Student (unique constraint)
SemesterResult aggregates per Student per Semester
AuditLog records actorUser, action, entityType, entityId, oldValue, newValue, timestamp

## 9. Important Business Rules

- A student is never directly tied to one semester; academic position derives from latest active StudentEnrollment
- Attendance and Marks always trace back to a SubjectOffering/TeachingSession
- Published results are immutable except through Correction Request -> Approval -> Update -> Audit Log
- Teachers can only act within their own SubjectOffering assignments
- Grading scheme is data-driven, never hardcoded thresholds

## 10-12. Timetable, Attendance, Result Design

See full architecture notes — SubjectOffering, TeachingSession, GradingScheme/GradeBand, MarksEntry, SemesterResult designs as discussed.

## 13-17. Folder Structure, Repo Structure, Branching, Roadmap

Backend: controller/service/repository/entity/dto/mapper/exception/security/config/audit/util

Frontend: components/pages/layouts/services/hooks/context/routes/types/utils

Repo: backend/, frontend/, database/, docs/, .gitignore, README.md

Branching: main, develop, feature/*, fix/*

Roadmap: 17 phases from Backend Foundation through Deployment, per project roadmap.