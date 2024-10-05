package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.request.ProductRequest;
import com.moldi_sams.se_project.response.ProductResponse;
import com.moldi_sams.se_project.service.implementation.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/product-id={productId}")
    public ResponseEntity<ProductResponse> findById(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.findById(productId));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(@RequestBody @Valid ProductRequest productRequest) {
        return ResponseEntity.created(
                URI.create("")
        ).body(productService.save(productRequest));
    }

    @PatchMapping("/product-id={productId}")
    public ResponseEntity<ProductResponse> updateById(@PathVariable("productId") Long productId, @RequestBody @Valid ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateById(productId, productRequest));
    }

    @DeleteMapping("/product-id={productId}")
    public ResponseEntity<Void> deleteById(@PathVariable("productId") Long productId) {
        productService.deleteById(productId);

        return ResponseEntity.ok().build();
    }
}
