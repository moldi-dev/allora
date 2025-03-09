package com.moldi.allora.request.user;

import com.moldi.allora.validation.StringMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@StringMatch(firstFieldName = "password", secondFieldName = "confirmPassword", errorFieldName = "confirmPassword", message = "The passwords do not match")
public record SignUpRequest(
        @NotEmpty(message = "The username is required")
        @Size(min = 8, max = 20, message = "The username must contain at least 8 and at most 20 characters")
        String username,

        @NotEmpty(message = "The email is required")
        @Email(message = "The email must follow the 'email@domain.com' pattern")
        @Size(max = 50, message = "The email must contain at most 50 characters")
        String email,

        @NotEmpty(message = "The firstFieldName name is required")
        @Size(max = 30, message = "The firstFieldName name must contain at most 30 characters")
        String firstName,

        @NotEmpty(message = "The last name is required")
        @Size(max = 30, message = "The last name must contain at most 30 characters")
        String lastName,

        @NotEmpty(message = "The address is required")
        @Size(max = 1000, message = "The address must contain at most 1000 characters")
        String address,

        @NotEmpty(message = "The password is required")
        @Size(min = 8, max = 30, message = "The password must contain at least 8 and at most 30 characters")
        String password,

        @NotEmpty(message = "The password confirmation is required")
        @Size(min = 8, max = 30, message = "The password confirmation must contain at least 8 and at most 30 characters")
        String confirmPassword,

        @NotEmpty(message = "The recaptcha is required")
        String recaptchaToken
) {
}
