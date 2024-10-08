package com.moldi_sams.se_project.request.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotEmpty(message = "The username is required")
        @Size(max = 50, message = "The username must contain at most 50 characters")
        String username,

        @NotEmpty(message = "The password is required")
        @Size(min = 8, max = 30, message = "The password must contain at least 8 and at most 30 characters")
        String password
) {
}
