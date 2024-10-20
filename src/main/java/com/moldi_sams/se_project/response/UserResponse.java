package com.moldi_sams.se_project.response;

public record UserResponse(
        Long userId,
        String username,
        String email,
        Boolean isAdministrator,
        UserPersonalInformationResponse userPersonalInformation
) {
}
