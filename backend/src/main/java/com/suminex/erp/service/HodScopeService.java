package com.suminex.erp.service;

import com.suminex.erp.dto.SubjectResponse;
import com.suminex.erp.dto.TeacherResponse;
import com.suminex.erp.entity.Student;
import com.suminex.erp.entity.StudentEnrollment;
import com.suminex.erp.entity.Subject;
import com.suminex.erp.entity.Teacher;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.StudentEnrollmentRepository;
import com.suminex.erp.repository.SubjectRepository;
import com.suminex.erp.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HodScopeService {

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final StudentEnrollmentRepository enrollmentRepository;

    public HodScopeService(TeacherRepository teacherRepository, SubjectRepository subjectRepository,
                           StudentEnrollmentRepository enrollmentRepository) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Resolves the calling HOD's own department from their userId — this is what
     * makes every "my-department" endpoint self-scoping and tamper-proof: the
     * department is never taken from the request, only ever derived from who the
     * logged-in user actually is.
     */
    private Long resolveHodDepartmentId(Long userId) {
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No teacher profile found for this account"));

        if (teacher.getDepartment() == null) {
            throw new ResourceNotFoundException(
                    "Your account is not currently assigned to a department. Contact an administrator.");
        }

        return teacher.getDepartment().getId();
    }

    public List<TeacherResponse> getMyDepartmentTeachers(Long userId) {
        Long departmentId = resolveHodDepartmentId(userId);
        return teacherRepository.findByDepartmentId(departmentId).stream()
                .map(this::toTeacherResponse)
                .collect(Collectors.toList());
    }

    public List<SubjectResponse> getMyDepartmentSubjects(Long userId) {
        Long departmentId = resolveHodDepartmentId(userId);
        return subjectRepository.findByCourseProgramDepartmentId(departmentId).stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());
    }

    public List<StudentSummary> getMyDepartmentStudents(Long userId) {
        Long departmentId = resolveHodDepartmentId(userId);
        List<StudentEnrollment> enrollments = enrollmentRepository.findActiveByDepartmentId(departmentId);
        return enrollments.stream()
                .map(e -> {
                    Student s = e.getStudent();
                    return new StudentSummary(
                            s.getId(),
                            s.getFirstName() + " " + s.getLastName(),
                            s.getRollNumber(),
                            e.getDivision().getDivisionName(),
                            e.getSemester().getSemesterNumber()
                    );
                })
                .collect(Collectors.toList());
    }

    private TeacherResponse toTeacherResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getUser().getId(),
                teacher.getUser().getEmail(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmployeeCode(),
                teacher.getDesignation(),
                teacher.getDepartment().getId(),
                teacher.getDepartment().getName()
        );
    }

    private SubjectResponse toSubjectResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getCode(),
                subject.getName(),
                subject.getCredits(),
                subject.getSubjectType(),
                subject.getCourseProgram().getId(),
                subject.getCourseProgram().getName(),
                subject.getSemester().getId(),
                subject.getSemester().getSemesterNumber()
        );
    }

    public static class StudentSummary {
        private Long studentId;
        private String studentName;
        private String rollNumber;
        private String divisionName;
        private int semesterNumber;

        public StudentSummary(Long studentId, String studentName, String rollNumber,
                              String divisionName, int semesterNumber) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.rollNumber = rollNumber;
            this.divisionName = divisionName;
            this.semesterNumber = semesterNumber;
        }

        public Long getStudentId() {
            return studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public int getSemesterNumber() {
            return semesterNumber;
        }
    }
}