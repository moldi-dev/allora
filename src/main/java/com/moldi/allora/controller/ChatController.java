package com.moldi.allora.controller;

import com.moldi.allora.request.admin.PublicChatMessageRequest;
import com.moldi.allora.response.PublicChatMessageResponse;
import com.moldi.allora.service.implementation.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-messages")
public class ChatController {
    public final ChatService chatService;
    private final SimpMessagingTemplate template;

    @PostMapping("/authenticated/connect-to-public-chat")
    public PublicChatMessageResponse connectToPublicChat(Authentication connectedUser) {
        var result = chatService.connectToPublicChat(connectedUser);

        template.convertAndSend("/chatroom/public", result);

        return result;
    }

    @PostMapping("/authenticated/disconnect-from-public-chat")
    public PublicChatMessageResponse disconnectFromPublicChat(Authentication connectedUser) {
        var result = chatService.disconnectFromPublicChat(connectedUser);

        template.convertAndSend("/chatroom/public", result);

        return result;
    }

    @PostMapping("/authenticated/send-public-message")
    public PublicChatMessageResponse sendPublicMessage(Authentication connectedUser, @RequestBody @Valid PublicChatMessageRequest request) {
        var result = chatService.sendPublicMessage(connectedUser, request);

        template.convertAndSend("/chatroom/public", result);

        return result;
    }
}
