package com.moldi.allora.service;

import com.moldi.allora.response.ProductCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IProductCategoryService {
    Page<ProductCategoryResponse> findAll(Pageable pageable);
    ProductCategoryResponse findById(Long productCategoryId);
    ProductCategoryResponse findByNameIgnoreCase(String name);
}
