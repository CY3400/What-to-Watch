package com.whattowatch.service;

import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.whattowatch.dto.AuthResponse;
import com.whattowatch.dto.AuthResult;
import com.whattowatch.dto.LoginRequest;
import com.whattowatch.dto.RegisterRequest;
import com.whattowatch.entity.User;
import com.whattowatch.exception.EmailAlreadyExistsException;
import com.whattowatch.exception.InvalidCredentialsException;
import com.whattowatch.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResult register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        String rawPassword = request.getPassword();

        if(userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException("Email déjà utilisé");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        try {
            User savedUser = userRepository.saveAndFlush(user);

            AuthResponse response =  buildAuthResponse(savedUser, "Inscription réussie");

            String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole().name());

            return buildAuthResult(response, token);
        }
        catch (DataIntegrityViolationException ex) {
            throw new EmailAlreadyExistsException("Email déjà utilisé");
        }
    }

    @Transactional(readOnly = true)
    public AuthResult login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());
        String password = request.getPassword();
        
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new InvalidCredentialsException("Identifiants invalides"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Identifiants invalides");
        }

        AuthResponse response =  buildAuthResponse(user, "Connexion réussie");

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return buildAuthResult(response, token);
    }

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmailIgnoreCase(normalizeEmail(email));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private AuthResponse buildAuthResponse(User user, String message) {
        return new AuthResponse(user.getId(), user.getEmail(), user.getRole(), message);
    }

    private AuthResult buildAuthResult(AuthResponse response, String token) {
        return new AuthResult(response, token);
    }
}
