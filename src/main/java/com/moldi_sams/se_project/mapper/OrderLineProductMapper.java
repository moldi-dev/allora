package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.OrderLineProduct;
import com.moldi_sams.se_project.response.OrderLineProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineProductMapper {
    private final ProductMapper productMapper;
    private final ProductSizeMapper productSizeMapper;

    public OrderLineProductResponse toOrderLineProductResponse(OrderLineProduct orderLineProduct) {
        return new OrderLineProductResponse(
                orderLineProduct.getOrderLineProductId(),
                productMapper.toProductResponse(orderLineProduct.getProduct()),
                orderLineProduct.getQuantity(),
                productSizeMapper.toProductSizeResponse(orderLineProduct.getProductSize())
        );
    }
}
