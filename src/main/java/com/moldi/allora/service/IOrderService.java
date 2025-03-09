package com.moldi.allora.service;

import com.moldi.allora.request.admin.OrderUpdateRequest;
import com.moldi.allora.request.user.OrderRequest;
import com.moldi.allora.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public interface IOrderService {
    Page<OrderResponse> findAll(Pageable pageable);
    OrderResponse findById(Long orderId);
    Page<OrderResponse> findAuthenticatedUserData(Authentication authentication, Pageable pageable);
    String placeOrder(Authentication authentication, OrderRequest orderRequest);
    String payPendingOrder(Authentication authentication, Long orderId);
    void deleteById(Long orderId);
    OrderResponse updateById(Long orderId, OrderUpdateRequest request);
}
