package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.response.HttpResponse;
import com.moldi_sams.se_project.service.implementation.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @GetMapping
    public ResponseEntity<HttpResponse> findAll(Pageable pageable) {
        var result = productCategoryService.findAll(pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product categories have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/product-category-id={productCategoryId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("productCategoryId") Long productCategoryId) {
        var result = productCategoryService.findById(productCategoryId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product category has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/name={name}")
    public ResponseEntity<HttpResponse> findByName(@PathVariable("name") String name) {
        var result = productCategoryService.findByNameIgnoreCase(name);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product category has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
