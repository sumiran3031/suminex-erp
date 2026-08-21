package com.suminex.erp.dto;

import com.suminex.erp.entity.Role;

public class AuthenticatedUserResponse {

    private Long id;
    private String email;
    private Role role;

    public AuthenticatedUserResponse(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}