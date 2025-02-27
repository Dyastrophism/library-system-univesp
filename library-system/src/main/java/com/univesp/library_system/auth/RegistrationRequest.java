package com.univesp.library_system.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record
RegistrationRequest(

    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name is required")
    String firstName,
    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name is required")
    String lastName,
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    String email,
    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password) {
}
