package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.ProductCategory;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductCategoryMapper;
import com.moldi.allora.repository.ProductCategoryRepository;
import com.moldi.allora.response.ProductCategoryResponse;
import com.moldi.allora.service.IProductCategoryService;
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
