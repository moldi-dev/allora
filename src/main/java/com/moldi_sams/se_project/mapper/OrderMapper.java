package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.Order;
import com.moldi_sams.se_project.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final UserPersonalInformationMapper userPersonalInformationMapper;
    private final ProductMapper productMapper;
    private final OrderLineProductMapper orderLineProductMapper;

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getOrderLineProducts().stream().map(orderLineProductMapper::toOrderLineProductResponse).toList(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                userPersonalInformationMapper.toUserPersonalInformationResponse(order.getUserPersonalInformation()),
                order.getOrderDate()
        );
    }
}
