package com.moldi_sams.se_project.request.user;

import com.moldi_sams.se_project.validation.FieldMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "newPassword", second = "confirmNewPassword", message = "The new passwords do not match")
public record PasswordChangeRequest(
        @NotEmpty(message = "The new password is required")
        @Size(min = 8, max = 30, message = "The current password must contain at least 8 and at most 30 characters")
        String currentPassword,

        @NotEmpty(message = "The new password confirmation is required")
        @Size(min = 8, max = 30, message = "The new password confirmation must contain at least 8 and at most 30 characters")
        String newPassword,

        @NotEmpty(message = "The new password confirmation is required")
        @Size(min = 8, max = 30, message = "The new password confirmation must contain at least 8 and at most 30 characters")
        String confirmNewPassword
) {
}
