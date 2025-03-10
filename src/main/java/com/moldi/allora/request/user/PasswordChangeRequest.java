package com.moldi.allora.request.user;

import com.moldi.allora.validation.StringMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@StringMatch(firstFieldName = "newPassword", secondFieldName = "confirmNewPassword", errorFieldName = "confirmNewPassword", message = "The new passwords do not match")
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
