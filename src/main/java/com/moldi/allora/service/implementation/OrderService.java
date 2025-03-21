package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.*;
import com.moldi.allora.enumeration.OrderStatus;
import com.moldi.allora.exception.BadRequestException;
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
import com.moldi.allora.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;

    @Override
    public Page<OrderResponse> findAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders could be found");
        }

        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse findById(Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id couldn't be found"));
    }

    @Override
    public Page<OrderResponse> findAuthenticatedUserData(Authentication authentication, Pageable pageable) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        Page<Order> orders = orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(personalInformation.getUserPersonalInformationId(), pageable);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders could be found");
        }

        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public String placeOrder(Authentication authentication, OrderRequest orderRequest) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        List<OrderLineProduct> orderLineProducts = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderLineProductRequest orderLineProductRequest : orderRequest.orderLineProducts()) {
            Product product = productRepository.findById(orderLineProductRequest.productId())
                    .orElseThrow(() -> new RuntimeException("The product by the provided id couldn't be found"));

            if (product.getStock() - orderLineProductRequest.quantity() < 0) {
                throw new BadRequestException("The product by the provided id can't be bought as there isn't enough stock available");
            }

            product.setStock(product.getStock() - orderLineProductRequest.quantity());

            ProductSize productSize = productSizeRepository.findById(orderLineProductRequest.productSizeId())
                    .orElseThrow(() -> new RuntimeException("The product size by the provided id couldn't be found"));

            OrderLineProduct orderLineProduct = OrderLineProduct.builder()
                    .product(product)
                    .quantity(orderLineProductRequest.quantity())
                    .productSize(productSize)
                    .build();

            BigDecimal linePrice = product.getPrice().multiply(BigDecimal.valueOf(orderLineProductRequest.quantity()));
            totalPrice = totalPrice.add(linePrice);

            orderLineProducts.add(orderLineProduct);

            productRepository.save(product);
        }

        Order newOrder = Order.builder()
                .userPersonalInformation(personalInformation)
                .orderLineProducts(orderLineProducts)
                .totalPrice(totalPrice)
                .orderStatus(OrderStatus.PENDING)
                .build();

        OrderResponse savedOrder = orderMapper.toOrderResponse(orderRepository.save(newOrder));

        return paymentService.createPaymentLink(savedOrder);
    }

    @Override
    public String payPendingOrder(Authentication authentication, Long orderId) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        Order searchedOrder = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id could not be found"));

        Page<Order> userOrders = orderRepository.findAllByUserPersonalInformationUserPersonalInformationId(personalInformation.getUserPersonalInformationId(), null);

        if (userOrders.isEmpty()) {
            throw new ResourceNotFoundException("The order by the provided id couldn't be found");
        }

        List<Long> userOrdersIds = userOrders
                .stream()
                .map(Order::getOrderId)
                .toList();

        if (!userOrdersIds.contains(orderId)) {
            throw new ResourceNotFoundException("The order by the provided id couldn't be found");
        }

        if (searchedOrder.getOrderStatus().equals(OrderStatus.PAID)) {
            throw new ResourceAlreadyExistsException("This order is already paid");
        }

        OrderResponse orderResponse = orderMapper.toOrderResponse(searchedOrder);

        return paymentService.createPaymentLink(orderResponse);
    }

    @Override
    public void deleteById(Long orderId) {
        Order searchedOrderById = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id couldn't be found"));

        orderRepository.delete(searchedOrderById);
    }

    @Override
    public OrderResponse updateById(Long orderId, OrderUpdateRequest request) {
        Order searchedOrderById = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id couldn't be found"));

        searchedOrderById.setOrderStatus(OrderStatus.valueOf(request.orderStatus()));

        return orderMapper.toOrderResponse(orderRepository.save(searchedOrderById));
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAllPendingOrdersDailyAtMidnight() {
        Page<Order> pendingOrders = orderRepository.findAllByOrderStatus(OrderStatus.PENDING, null);
        orderRepository.deleteAll(pendingOrders);
    }
}
