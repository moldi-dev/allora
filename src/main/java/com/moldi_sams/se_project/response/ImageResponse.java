package com.moldi_sams.se_project.response;

import java.math.BigDecimal;

public record ImageResponse(
        Long imageId,
        String name,
        BigDecimal size,
        String type,
        String url
) {
}
