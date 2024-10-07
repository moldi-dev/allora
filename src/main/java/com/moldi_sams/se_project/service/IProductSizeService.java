package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.response.ProductSizeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductSizeService {
    List<ProductSizeResponse> findAll();
    ProductSizeResponse findById(Long productSizeId);
    ProductSizeResponse findByNameIgnoreCase(String name);
}
