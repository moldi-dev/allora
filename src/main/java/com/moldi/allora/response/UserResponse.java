package com.moldi.allora.response;

public record UserResponse(
        Long userId,
        String username,
        String email,
        Boolean isAdministrator,
        UserPersonalInformationResponse userPersonalInformation
) {
}
