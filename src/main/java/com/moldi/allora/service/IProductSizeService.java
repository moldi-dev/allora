package com.moldi.allora.service;

import com.moldi.allora.response.ProductSizeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductSizeService {
    List<ProductSizeResponse> findAll();
    ProductSizeResponse findById(Long productSizeId);
    ProductSizeResponse findByNameIgnoreCase(String name);
}
