package com.moldi.allora.response;

public record UserPersonalInformationResponse(
        Long userPersonalInformationId,
        String firstName,
        String lastName,
        String address
) {
}
