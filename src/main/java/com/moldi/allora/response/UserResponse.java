package com.moldi.allora.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String username,
        String email,
        Boolean isAdministrator,
        UserPersonalInformationResponse userPersonalInformation,
        LocalDateTime lastLogin
) {
}
