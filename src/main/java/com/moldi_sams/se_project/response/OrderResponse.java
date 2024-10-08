package com.moldi_sams.se_project.response;

import com.moldi_sams.se_project.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long orderId,
    List<OrderLineProductResponse> orderLineProducts,
    BigDecimal totalPrice,
    OrderStatus orderStatus,
    UserPersonalInformationResponse userPersonalInformation,
    LocalDateTime orderDate
) {
}
