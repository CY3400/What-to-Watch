package com.whattowatch.dto;

import com.whattowatch.entity.Role;

public class AuthResponse {
    private final Long userId;
    private final String email;
    private final Role role;
    private final String message;

    public AuthResponse(Long userId, String email, Role role, String message) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    public Long getUserId(){
        return userId;
    }

    public String getEmail(){
        return email;
    }

    public Role getRole(){
        return role;
    }

    public String getMessage(){
        return message;
    }
}
