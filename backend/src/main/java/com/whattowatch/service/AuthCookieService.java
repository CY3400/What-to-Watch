package com.whattowatch.service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.whattowatch.config.AppProps;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthCookieService {
    private static final String COOKIE_NAME = "jwt_token";

    private final Duration cookieMaxAge;

    public AuthCookieService(AppProps appProps) {
        this.cookieMaxAge = Duration.ofMillis(appProps.getJwt().getExpirationMs());
    }

    public void addCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token == null ? "" : token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(this.cookieMaxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if(COOKIE_NAME.equals(cookie.getName())) {
                String tokenFromCookie = cookie.getValue();
                return tokenFromCookie == null || tokenFromCookie.isBlank() ? null : tokenFromCookie;
            }
        }

        return null;
    }
}
