package com.suminex.erp.controller;

import com.suminex.erp.dto.AuthenticatedUserResponse;
import com.suminex.erp.dto.CreateUserRequest;
import com.suminex.erp.dto.UserResponse;
import com.suminex.erp.security.CustomUserDetails;
import com.suminex.erp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AuthenticatedUserResponse response = new AuthenticatedUserResponse(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getRole()
        );
        return ResponseEntity.ok(response);
    }
}