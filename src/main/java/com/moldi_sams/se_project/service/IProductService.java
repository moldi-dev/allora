package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.request.admin.ProductRequest;
import com.moldi_sams.se_project.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {
    Page<ProductResponse> findAll(Pageable pageable);
    ProductResponse findById(Long productId);
    ProductResponse save(ProductRequest productRequest);
    ProductResponse updateById(Long productId, ProductRequest productRequest);
    void deleteById(Long productId);
}
