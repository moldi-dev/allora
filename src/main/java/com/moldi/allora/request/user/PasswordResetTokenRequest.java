package com.moldi.allora.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PasswordResetTokenRequest(
        @NotEmpty(message = "The email is required")
        @Email(message = "The email must follow the 'email@domain.com' pattern")
        @Size(max = 50, message = "The email must contain at most 50 characters")
        String email,

        @NotEmpty(message = "The recaptcha is required")
        String recaptchaToken
) {
}
