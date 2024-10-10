package com.moldi_sams.se_project.request.user;

import com.moldi_sams.se_project.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "newPassword", second = "confirmNewPassword", message = "The passwords do not match")
public record PasswordResetRequest(
        @NotEmpty(message = "The email is required")
        @Email(message = "The email must follow the 'email@domain.com' pattern")
        @Size(max = 50, message = "The email must contain at most 50 characters")
        String email,

        @NotEmpty(message = "The password reset code is required")
        String resetPasswordCode,

        @NotEmpty(message = "The new password is required")
        @Size(min = 8, max = 30, message = "The new password must contain at least 8 and at most 30 characters")
        String newPassword,

        @NotEmpty(message = "The new password confirmation is required")
        @Size(min = 8, max = 30, message = "The new password confirmation must contain at least 8 and at most 30 characters")
        String confirmNewPassword,

        @NotEmpty(message = "The recaptcha is required")
        String recaptchaToken
) {
}
