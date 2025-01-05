package com.moldi_sams.se_project.request.admin;

import jakarta.validation.constraints.NotEmpty;

public record AiPromptRequest(
        @NotEmpty(message = "The prompt is required")
        String prompt
) {
}
