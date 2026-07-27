package com.whattowatch.dto;

public class AuthResult {
    private final UserSessionResponse response;
    private final String token;

    public AuthResult(UserSessionResponse response, String token) {
        this.response = response;
        this.token = token;
    }

    public UserSessionResponse getResponse(){
        return response;
    }

    public String getToken(){
        return token;
    }
}
