package com.suminex.erp.service;

import com.suminex.erp.dto.CreateStaffRequest;
import com.suminex.erp.dto.StaffResponse;
import com.suminex.erp.entity.Role;
import com.suminex.erp.entity.Staff;
import com.suminex.erp.entity.User;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.repository.StaffRepository;
import com.suminex.erp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffService(StaffRepository staffRepository, UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("A user with this email already exists");
        }

        if (staffRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ConflictException("A staff member with this employee code already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STAFF);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Staff staff = new Staff();
        staff.setUser(savedUser);
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setEmployeeCode(request.getEmployeeCode());
        staff.setResponsibility(request.getResponsibility());
        Staff savedStaff = staffRepository.save(staff);

        return toResponse(savedStaff);
    }

    private StaffResponse toResponse(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                staff.getUser().getId(),
                staff.getUser().getEmail(),
                staff.getFirstName(),
                staff.getLastName(),
                staff.getEmployeeCode(),
                staff.getResponsibility()
        );
    }
}