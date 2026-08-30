package com.suminex.erp.service;

import com.suminex.erp.dto.CreateStudentRequest;
import com.suminex.erp.dto.StudentResponse;
import com.suminex.erp.entity.Role;
import com.suminex.erp.entity.Student;
import com.suminex.erp.entity.User;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.repository.StudentRepository;
import com.suminex.erp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("A user with this email already exists");
        }

        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException("A user with this phone number already exists");
        }

        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new ConflictException("A student with this roll number already exists");
        }

        if (request.getPrn() != null && studentRepository.existsByPrn(request.getPrn())) {
            throw new ConflictException("A student with this PRN already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setRollNumber(request.getRollNumber());
        student.setPrn(request.getPrn());
        student.setDateOfBirth(request.getDateOfBirth());
        Student savedStudent = studentRepository.save(student);

        return toResponse(savedStudent);
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getUser().getId(),
                student.getUser().getEmail(),
                student.getFirstName(),
                student.getLastName(),
                student.getRollNumber(),
                student.getPrn(),
                student.getDateOfBirth()
        );
    }
}