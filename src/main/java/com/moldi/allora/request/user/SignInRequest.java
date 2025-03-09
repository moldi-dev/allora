package com.moldi.allora.request.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotEmpty(message = "The username is required")
        @Size(min = 8, max = 20, message = "The username must contain at least 8 and at most 20 characters")
        String username,

        @NotEmpty(message = "The password is required")
        @Size(min = 8, max = 30, message = "The password must contain at least 8 and at most 30 characters")
        String password,

        @NotEmpty(message = "The recaptcha is required")
        String recaptchaToken
) {
}
