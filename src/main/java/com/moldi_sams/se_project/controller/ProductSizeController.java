package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.response.HttpResponse;
import com.moldi_sams.se_project.service.implementation.ProductSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-sizes")
public class ProductSizeController {
    private final ProductSizeService productSizeService;

    @GetMapping
    public ResponseEntity<HttpResponse> findAll() {
        var result = productSizeService.findAll();

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product sizes have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/product-size-id={productSizeId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("productSizeId") Long productSizeId) {
        var result = productSizeService.findById(productSizeId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product size has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/name={name}")
    public ResponseEntity<HttpResponse> findByName(@PathVariable("name") String name) {
        var result = productSizeService.findByNameIgnoreCase(name);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product size has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
