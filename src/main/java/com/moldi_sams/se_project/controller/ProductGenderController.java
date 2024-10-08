package com.moldi_sams.se_project.controller;

import com.moldi_sams.se_project.response.HttpResponse;
import com.moldi_sams.se_project.service.implementation.ProductGenderService;
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
@RequestMapping("/api/v1/product-genders")
public class ProductGenderController {
    private final ProductGenderService productGenderService;

    @GetMapping
    public ResponseEntity<HttpResponse> findAll() {
        var result = productGenderService.findAll();

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product genders have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/id={productGenderId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("productGenderId") Long productGenderId) {
        var result = productGenderService.findById(productGenderId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product gender has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/name={name}")
    public ResponseEntity<HttpResponse> findByName(@PathVariable("name") String name) {
        var result = productGenderService.findByNameIgnoreCase(name);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The product gender has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
