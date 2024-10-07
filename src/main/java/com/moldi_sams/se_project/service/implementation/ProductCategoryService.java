package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.ProductCategory;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.ProductCategoryMapper;
import com.moldi_sams.se_project.repository.ProductCategoryRepository;
import com.moldi_sams.se_project.response.ProductCategoryResponse;
import com.moldi_sams.se_project.service.IProductCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCategoryService implements IProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Override
    public Page<ProductCategoryResponse> findAll(Pageable pageable) {
        Page<ProductCategory> productCategories = productCategoryRepository.findAll(pageable);

        if (productCategories.isEmpty()) {
            throw new ResourceNotFoundException("No product categories could be found");
        }

        return productCategories.map(productCategoryMapper::toProductCategoryResponse);
    }

    @Override
    public ProductCategoryResponse findById(Long productCategoryId) {
        return productCategoryRepository
                .findById(productCategoryId)
                .map(productCategoryMapper::toProductCategoryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product category by the provided id could not be found"));
    }

    @Override
    public ProductCategoryResponse findByNameIgnoreCase(String name) {
        return productCategoryRepository
                .findByNameIgnoreCase(name)
                .map(productCategoryMapper::toProductCategoryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product category by the provided name could not be found"));
    }
}
