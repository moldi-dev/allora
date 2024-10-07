package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.response.ProductBrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IProductBrandService {
    Page<ProductBrandResponse> findAll(Pageable pageable);
    ProductBrandResponse findById(Long productBrandId);
    ProductBrandResponse findByNameIgnoreCase(String name);
}
