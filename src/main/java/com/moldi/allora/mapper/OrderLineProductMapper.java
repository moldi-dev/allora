package com.moldi.allora.mapper;

import com.moldi.allora.entity.OrderLineProduct;
import com.moldi.allora.response.OrderLineProductResponse;
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
