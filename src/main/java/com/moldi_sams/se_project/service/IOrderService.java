package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.request.user.OrderRequest;
import com.moldi_sams.se_project.response.OrderResponse;
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
}
