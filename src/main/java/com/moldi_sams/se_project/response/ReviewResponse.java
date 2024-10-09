package com.moldi_sams.se_project.response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewId,
        Long productId,
        String comment,
        Integer rating,
        String firstName,
        String lastName,
        LocalDateTime reviewDate
) {
}
