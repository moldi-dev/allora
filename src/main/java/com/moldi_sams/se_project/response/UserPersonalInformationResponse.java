package com.moldi_sams.se_project.response;

public record UserPersonalInformationResponse(
        Long userPersonalInformationId,
        String firstName,
        String lastName,
        String address
) {
}
