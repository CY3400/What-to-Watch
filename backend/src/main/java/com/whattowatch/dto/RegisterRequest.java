package com.whattowatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message="L'email est obligatoire")
    @Email(message="Email invalide")
    @Size(max = 190, message="L'email ne doit pas dépasser 190 caractères")
    private String email;
    @NotBlank(message="Le mot de passe est obligatoire")
    @Size(min = 10, max = 64, message="Le mot de passe doit être entre 10 et 64 caractères")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])[\\x20-\\x7E]{10,64}$", message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial")
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