package com.moldi_sams.se_project.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserPersonalInformationRequest(
        @NotEmpty(message = "The first name is required")
        @Size(max = 30, message = "The first name must contain at most 30 characters")
        String firstName,

        @NotEmpty(message = "The last name is required")
        @Size(max = 30, message = "The last name must contain at most 30 characters")
        String lastName,

        @NotEmpty(message = "The address is required")
        @Size(max = 1000, message = "The address must contain at most 1000 characters")
        String address
) {
}
