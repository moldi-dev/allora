package com.moldi.allora.controller;

import com.moldi.allora.request.admin.OrderUpdateRequest;
import com.moldi.allora.request.user.OrderRequest;
import com.moldi.allora.response.HttpResponse;
import com.moldi.allora.service.implementation.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<HttpResponse> findAll(Pageable pageable) {
        var result = orderService.findAll(pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The orders have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/id={orderId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("orderId") Long orderId) {
        var result = orderService.findById(orderId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The order has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/authenticated")
    public ResponseEntity<HttpResponse> findAuthenticatedUserData(Authentication authentication, Pageable pageable) {
        var result = orderService.findAuthenticatedUserData(authentication, pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The orders have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<HttpResponse> placeOrder(Authentication authentication, @RequestBody @Valid OrderRequest orderRequest) {
        var result = orderService.placeOrder(authentication, orderRequest);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage(result)
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/id={orderId}")
    public ResponseEntity<HttpResponse> updateById(@PathVariable("orderId") Long orderId, @RequestBody @Valid OrderUpdateRequest request) {
        var result = orderService.updateById(orderId, request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The order has been updated successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @PatchMapping("/pending/id={orderId}")
    public ResponseEntity<HttpResponse> payPendingOrder(@PathVariable("orderId") Long orderId, Authentication authentication) {
        var result = orderService.payPendingOrder(authentication, orderId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage(result)
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/id={orderId}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable("orderId") Long orderId) {
        orderService.deleteById(orderId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The order has been deleted successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
