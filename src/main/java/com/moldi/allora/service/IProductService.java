package com.moldi.allora.service;

import com.moldi.allora.request.admin.ProductRequest;
import com.moldi.allora.request.user.ProductFilterRequest;
import com.moldi.allora.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {
    Page<ProductResponse> findAll(Pageable pageable);
    Page<ProductResponse> findAllInStock(Pageable pageable);
    Page<ProductResponse> findAllByFilters(ProductFilterRequest request, Pageable pageable);
    ProductResponse findById(Long productId);
    ProductResponse save(ProductRequest productRequest);
    ProductResponse updateById(Long productId, ProductRequest productRequest);
    void deleteById(Long productId);
}
