package com.whattowatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whattowatch.dto.UserSessionResponse;
import com.whattowatch.dto.AuthResult;
import com.whattowatch.dto.LoginRequest;
import com.whattowatch.dto.RegisterRequest;
import com.whattowatch.security.UserPrincipal;
import com.whattowatch.service.AuthCookieService;
import com.whattowatch.service.AuthService;
import com.whattowatch.service.CurrentUserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final CurrentUserService currentUserService;
    private final AuthCookieService authCookieService;

    public AuthController(AuthService authService, CurrentUserService currentUserService, AuthCookieService authCookieService) {
        this.authService = authService;
        this.currentUserService = currentUserService;
        this.authCookieService = authCookieService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserSessionResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResult result = authService.register(request);

        String token = result.getToken();

        authCookieService.addCookie(response, token);

        return ResponseEntity.status(201).body(result.getResponse());
    }

    @PostMapping("/login")
    public ResponseEntity<UserSessionResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResult result = authService.login(request);

        String token = result.getToken();

        authCookieService.addCookie(response, token);

        return ResponseEntity.ok(result.getResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authCookieService.deleteCookie(response);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserSessionResponse> me(Authentication authentication) {
        UserPrincipal user = currentUserService.getCurrentUser(authentication);

        UserSessionResponse response = new UserSessionResponse(user.getId(), user.getUsername(), user.getRole());

        return ResponseEntity.ok(response);
    }
}
