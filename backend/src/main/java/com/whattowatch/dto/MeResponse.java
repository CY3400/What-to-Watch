package com.whattowatch.dto;

import com.whattowatch.entity.Role;

public class MeResponse {
    private final Long userId;
    private final String email;
    private final Role role;

    public MeResponse(Long userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
