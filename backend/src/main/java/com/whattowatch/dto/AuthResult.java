package com.whattowatch.dto;

public class AuthResult {
    private final AuthResponse response;
    private final String token;

    public AuthResult(AuthResponse response, String token) {
        this.response = response;
        this.token = token;
    }

    public AuthResponse getResponse(){
        return response;
    }

    public String getToken(){
        return token;
    }
}
