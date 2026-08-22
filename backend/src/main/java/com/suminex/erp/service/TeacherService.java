package com.suminex.erp.service;

import com.suminex.erp.dto.CreateTeacherRequest;
import com.suminex.erp.dto.TeacherResponse;
import com.suminex.erp.entity.Role;
import com.suminex.erp.entity.Teacher;
import com.suminex.erp.entity.User;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.repository.TeacherRepository;
import com.suminex.erp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public TeacherResponse createTeacher(CreateTeacherRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("A user with this email already exists");
        }

        if (teacherRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ConflictException("A teacher with this employee code already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.TEACHER);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmployeeCode(request.getEmployeeCode());
        teacher.setDesignation(request.getDesignation());
        Teacher savedTeacher = teacherRepository.save(teacher);

        return toResponse(savedTeacher);
    }

    private TeacherResponse toResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getUser().getId(),
                teacher.getUser().getEmail(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmployeeCode(),
                teacher.getDesignation()
        );
    }
}