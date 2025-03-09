package com.moldi.allora.request.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PublicChatMessageRequest(
        @NotEmpty(message = "The message is required")
        @Size(max = 500, message = "The message must contain at most 500 characters")
        String content
) {

}
