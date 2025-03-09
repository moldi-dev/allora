package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.User;
import com.moldi.allora.enumeration.ChatMessageType;
import com.moldi.allora.request.admin.PublicChatMessageRequest;
import com.moldi.allora.response.PublicChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    public PublicChatMessageResponse connectToPublicChat(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        return new PublicChatMessageResponse(
                user.getPersonalInformation().getUserPersonalInformationId(),
                user.getPersonalInformation().getFirstName(),
                user.getPersonalInformation().getLastName(),
                ChatMessageType.CONNECT,
                user.getPersonalInformation().getFirstName() + " " + user.getPersonalInformation().getLastName() + " has joined the chat!",
                LocalDateTime.now());
    }

    public PublicChatMessageResponse disconnectFromPublicChat(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        return new PublicChatMessageResponse(
                user.getPersonalInformation().getUserPersonalInformationId(),
                user.getPersonalInformation().getFirstName(),
                user.getPersonalInformation().getLastName(),
                ChatMessageType.DISCONNECT,
                user.getPersonalInformation().getFirstName() + " " + user.getPersonalInformation().getLastName() + " has left the chat!",
                LocalDateTime.now());
    }

    public PublicChatMessageResponse sendPublicMessage(Authentication connectedUser, PublicChatMessageRequest request) {
        User user = (User) connectedUser.getPrincipal();

        return new PublicChatMessageResponse(
                user.getPersonalInformation().getUserPersonalInformationId(),
                user.getPersonalInformation().getFirstName(),
                user.getPersonalInformation().getLastName(),
                ChatMessageType.MESSAGE,
                request.content(),
                LocalDateTime.now());
    }
}
