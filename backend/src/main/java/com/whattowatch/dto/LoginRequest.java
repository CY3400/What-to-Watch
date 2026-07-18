package com.whattowatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class LoginRequest {
    @NotBlank(message="L'email est obligatoire")
    @Email(message="Email invalide")
    @Size(max = 190, message="L'email ne doit pas dépasser 190 caractères")
    private String email;
    @NotBlank(message="Le mot de passe est obligatoire")
    @Size(max = 64, message="Le mot de passe ne doit pas dépasser 64 caractères")
    private String password;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
