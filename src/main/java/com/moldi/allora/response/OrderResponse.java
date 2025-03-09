package com.moldi.allora.response;

import com.moldi.allora.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long orderId,
    List<OrderLineProductResponse> orderLineProducts,
    BigDecimal totalPrice,
    OrderStatus orderStatus,
    UserPersonalInformationResponse userPersonalInformation,
    LocalDateTime createdDate
) {
}
