package com.moldi.allora.response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewId,
        Long productId,
        String comment,
        Integer rating,
        String firstName,
        String lastName,
        LocalDateTime createdDate
) {
}
