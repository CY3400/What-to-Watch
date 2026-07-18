package com.whattowatch.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.whattowatch.security.UserPrincipal;
import com.whattowatch.exception.InvalidCredentialsException;

@Service
public class CurrentUserService {
    public UserPrincipal getCurrentUser(Authentication auth) {
        if(auth == null || !auth.isAuthenticated()) {
            throw new InvalidCredentialsException("Utilisateur non authentifié");
        }

        Object principal = auth.getPrincipal();
        if(!(principal instanceof UserPrincipal userPrincipal)) {
            throw new InvalidCredentialsException("Principal utilisateur invalide");
        }
        
        return userPrincipal;
    }
}
