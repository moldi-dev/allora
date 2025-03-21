package com.moldi.allora.request.user;

import com.moldi.allora.validation.StringMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@StringMatch(firstFieldName = "newPassword", secondFieldName = "confirmNewPassword", errorFieldName = "confirmNewPassword", message = "The new passwords do not match")
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
