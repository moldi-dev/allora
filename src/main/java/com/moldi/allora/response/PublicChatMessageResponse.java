package com.moldi.allora.response;

import com.moldi.allora.enumeration.ChatMessageType;

import java.time.LocalDateTime;

public record PublicChatMessageResponse(
        Long personalInformationId,
        String firstName,
        String lastName,
        ChatMessageType messageType,
        String content,
        LocalDateTime createdDate
) {
}
