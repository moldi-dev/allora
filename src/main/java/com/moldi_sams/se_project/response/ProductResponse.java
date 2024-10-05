package com.moldi_sams.se_project.response;

import java.math.BigDecimal;

public record ProductResponse(
    Long productId,
    String productName,
    String productDescription,
    BigDecimal productPrice
) {
}
