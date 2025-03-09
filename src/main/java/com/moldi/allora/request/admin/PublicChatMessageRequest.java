package com.moldi.allora.request.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PublicChatMessageRequest(
        @NotEmpty(message = "The message is required")
        @Size(max = 200, message = "The message must contain at most 200 characters")
        String content
) {

}
