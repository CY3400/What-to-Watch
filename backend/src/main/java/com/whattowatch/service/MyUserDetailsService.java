package com.whattowatch.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.whattowatch.repository.UserRepository;
import com.whattowatch.security.UserPrincipal;
import com.whattowatch.util.EmailUtils;

@Service
@Transactional(readOnly = true)
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        String normalizedEmail = EmailUtils.normalize(email);

        if (normalizedEmail == null) {
            normalizedEmail = "";
        }

        return userRepository.findByEmailIgnoreCase(normalizedEmail).map(UserPrincipal::new).orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
    }
}