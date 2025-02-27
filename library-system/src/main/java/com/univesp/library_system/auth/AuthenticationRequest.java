package com.univesp.library_system.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
        @Email(message = "Email is invalid")
        @NotEmpty(message = "Email is required")
        @NotBlank(message = "Email is required")
        String email,
        @NotEmpty(message = "Password is required")
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) {
}
