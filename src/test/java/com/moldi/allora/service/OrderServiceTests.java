package com.moldi.allora.service;

import com.moldi.allora.entity.*;
import com.moldi.allora.enumeration.OrderStatus;
import com.moldi.allora.exception.ResourceAlreadyExistsException;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.OrderMapper;
import com.moldi.allora.repository.OrderRepository;
import com.moldi.allora.repository.ProductRepository;
import com.moldi.allora.repository.ProductSizeRepository;
import com.moldi.allora.request.admin.OrderUpdateRequest;
import com.moldi.allora.request.user.OrderLineProductRequest;
import com.moldi.allora.request.user.OrderRequest;
import com.moldi.allora.response.OrderResponse;
import com.moldi.allora.service.implementation.OrderService;
import com.moldi.allora.service.implementation.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSizeRepository productSizeRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderResponse orderResponse;
    private OrderRequest orderRequest;
    private OrderUpdateRequest orderUpdateRequest;
    private User user;
    private UserPersonalInformation personalInformation;
    private Product product;
    private ProductSize productSize;

    @BeforeEach
    void setUp() {
        user = new User();
        personalInformation = new UserPersonalInformation();
        personalInformation.setUserPersonalInformationId(1L);
        user.setPersonalInformation(personalInformation);

        product = Product.builder()
                .productId(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(100))
                .stock(10L)
                .build();

        productSize = ProductSize.builder()
                .productSizeId(1L)
                .name("Test Size")
                .build();

        order = Order.builder()
                .orderId(1L)
                .userPersonalInformation(personalInformation)
                .orderLineProducts(new ArrayList<>())
                .totalPrice(BigDecimal.valueOf(100))
                .orderStatus(OrderStatus.PENDING)
                .build();

        orderResponse = new OrderResponse(1L, Collections.emptyList(), BigDecimal.valueOf(100), OrderStatus.PENDING, null, null);

        orderRequest = new OrderRequest(List.of(new OrderLineProductRequest(1L, 1L, 2L)));

        orderUpdateRequest = new OrderUpdateRequest("PAID");
    }

    @Test
    void findAll_ShouldReturnPageOfOrderResponses_WhenOrdersExist() {
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        Page<OrderResponse> result = orderService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(orderResponse, result.getContent().get(0));

        verify(orderRepository, times(1)).findAll(any(Pageable.class));
        verify(orderMapper, times(1)).toOrderResponse(order);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoOrdersExist() {
        Page<Order> emptyPage = new PageImpl<>(Collections.emptyList());
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> orderService.findAll(Pageable.unpaged()));

        verify(orderRepository, times(1)).findAll(any(Pageable.class));
        verify(orderMapper, never()).toOrderResponse(any());
    }

    @Test
    void findById_ShouldReturnOrderResponse_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.findById(1L);

        assertNotNull(result);
        assertEquals(orderResponse.orderId(), result.orderId());
        assertEquals(orderResponse.totalPrice(), result.totalPrice());
        assertEquals(orderResponse.orderStatus(), result.orderStatus());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, times(1)).toOrderResponse(order);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.findById(1L));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, never()).toOrderResponse(any());
    }

    @Test
    void findAuthenticatedUserData_ShouldReturnPageOfOrderResponses_WhenOrdersExist() {
        when(authentication.getPrincipal()).thenReturn(user);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(1L, Pageable.unpaged())).thenReturn(orderPage);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        Page<OrderResponse> result = orderService.findAuthenticatedUserData(authentication, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(orderResponse, result.getContent().get(0));

        verify(authentication, times(1)).getPrincipal();
        verify(orderRepository, times(1)).findAllByUserPersonalInformationUserPersonalInformationId(1L, Pageable.unpaged());
        verify(orderMapper, times(1)).toOrderResponse(order);
    }

    @Test
    void findAuthenticatedUserData_ShouldThrowResourceNotFoundException_WhenNoOrdersExist() {
        when(authentication.getPrincipal()).thenReturn(user);
        Page<Order> emptyPage = new PageImpl<>(Collections.emptyList());
        when(orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(1L, Pageable.unpaged())).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> orderService.findAuthenticatedUserData(authentication, Pageable.unpaged()));

        verify(authentication, times(1)).getPrincipal();
        verify(orderRepository, times(1)).findAllByUserPersonalInformationUserPersonalInformationId(1L, Pageable.unpaged());
        verify(orderMapper, never()).toOrderResponse(any());
    }

    @Test
    void payPendingOrder_ShouldReturnPaymentLink_WhenOrderIsPending() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(1L, null)).thenReturn(new PageImpl<>(Collections.singletonList(order)));
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);
        when(paymentService.createPaymentLink(orderResponse)).thenReturn("paymentLink");

        String result = orderService.payPendingOrder(authentication, 1L);

        assertNotNull(result);
        assertEquals("paymentLink", result);

        verify(authentication, times(1)).getPrincipal();
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findAllByUserPersonalInformationUserPersonalInformationId(1L, null);
        verify(orderMapper, times(1)).toOrderResponse(order);
        verify(paymentService, times(1)).createPaymentLink(orderResponse);
    }

    @Test
    void payPendingOrder_ShouldThrowResourceNotFoundException_WhenOrderDoesNotExist() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.payPendingOrder(authentication, 1L));

        verify(authentication, times(1)).getPrincipal();
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).findAllByUserPersonalInformationUserPersonalInformationId(any(), any());
        verify(orderMapper, never()).toOrderResponse(any());
        verify(paymentService, never()).createPaymentLink(any());
    }

    @Test
    void payPendingOrder_ShouldThrowResourceAlreadyExistsException_WhenOrderIsAlreadyPaid() {
        when(authentication.getPrincipal()).thenReturn(user);
        order.setOrderStatus(OrderStatus.PAID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(1L, null)).thenReturn(new PageImpl<>(Collections.singletonList(order)));

        assertThrows(ResourceAlreadyExistsException.class, () -> orderService.payPendingOrder(authentication, 1L));

        verify(authentication, times(1)).getPrincipal();
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findAllByUserPersonalInformationUserPersonalInformationId(1L, null);
        verify(orderMapper, never()).toOrderResponse(any());
        verify(paymentService, never()).createPaymentLink(any());
    }

    @Test
    void deleteById_ShouldDeleteOrder_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteById(1L);

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void deleteById_ShouldThrowResourceNotFoundException_WhenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteById(1L));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).delete(any());
    }

    @Test
    void updateById_ShouldReturnOrderResponse_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.updateById(1L, orderUpdateRequest);

        assertNotNull(result);
        assertEquals(orderResponse.orderId(), result.orderId());
        assertEquals(orderResponse.orderStatus(), result.orderStatus());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toOrderResponse(order);
    }

    @Test
    void updateById_ShouldThrowResourceNotFoundException_WhenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateById(1L, orderUpdateRequest));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any());
        verify(orderMapper, never()).toOrderResponse(any());
    }

    @Test
    void deleteAllPendingOrdersDailyAtMidnight_ShouldDeletePendingOrders() {
        Page<Order> pendingOrders = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAllByOrderStatus(OrderStatus.PENDING, null)).thenReturn(pendingOrders);

        orderService.deleteAllPendingOrdersDailyAtMidnight();

        verify(orderRepository, times(1)).findAllByOrderStatus(OrderStatus.PENDING, null);
        verify(orderRepository, times(1)).deleteAll(pendingOrders);
    }
}