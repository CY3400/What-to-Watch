package com.whattowatch.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whattowatch.dto.AuthResponse;
import com.whattowatch.dto.AuthResult;
import com.whattowatch.dto.LoginRequest;
import com.whattowatch.dto.MeResponse;
import com.whattowatch.dto.RegisterRequest;
import com.whattowatch.dto.VerifyRequest;
import com.whattowatch.security.UserPrincipal;
import com.whattowatch.service.AuthService;
import com.whattowatch.service.CurrentUserService;
import com.whattowatch.config.AppProps;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final Duration cookieMaxAge;
    private final CurrentUserService currentUserService;

    public AuthController(AuthService authService, CurrentUserService currentUserService, AppProps appProps) {
        this.authService = authService;
        this.currentUserService = currentUserService;
        this.cookieMaxAge = Duration.ofMillis(appProps.getJwt().getExpirationMs());
    }

    private void setCookie(HttpServletResponse response, String value, Duration maxAge) {
        ResponseCookie cookie = ResponseCookie.from("jwt_token", value == null ? "" : value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResult result = authService.register(request);

        String token = result.getToken();

        setCookie(response, token, cookieMaxAge);

        return ResponseEntity.status(201).body(result.getResponse());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResult result = authService.login(request);

        String token = result.getToken();

        setCookie(response, token, cookieMaxAge);

        return ResponseEntity.ok(result.getResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        setCookie(response, null, Duration.ZERO);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email-available")
    public ResponseEntity<Boolean> verify(@Valid @RequestBody VerifyRequest res) {
        Boolean response = authService.isEmailAvailable(res.getEmail());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        UserPrincipal user = currentUserService.getCurrentUser(authentication);

        MeResponse response = new MeResponse(user.getId(), user.getUsername(), user.getRole());

        return ResponseEntity.ok(response);
    }
}
