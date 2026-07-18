package com.whattowatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class VerifyRequest {
    @NotBlank(message="L'email est obligatoire")
    @Email(message="Email invalide")
    @Size(max = 190, message = "L'email ne doit pas dépasser 190 caractères")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
