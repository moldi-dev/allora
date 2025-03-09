package com.moldi.allora.request.admin;

import jakarta.validation.constraints.NotEmpty;

public record AiPromptRequest(
        @NotEmpty(message = "The prompt is required")
        String prompt
) {
}
