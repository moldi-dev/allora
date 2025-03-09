package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.ProductBrand;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductBrandMapper;
import com.moldi.allora.repository.ProductBrandRepository;
import com.moldi.allora.response.ProductBrandResponse;
import com.moldi.allora.service.IProductBrandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductBrandService implements IProductBrandService {
    private final ProductBrandRepository productBrandRepository;
    private final ProductBrandMapper productBrandMapper;

    @Override
    public Page<ProductBrandResponse> findAll(Pageable pageable) {
        Page<ProductBrand> productBrands = productBrandRepository.findAll(pageable);

        if (productBrands.isEmpty()) {
            throw new ResourceNotFoundException("No product brands could be found");
        }

        return productBrands.map(productBrandMapper::toProductBrandResponse);
    }

    @Override
    public ProductBrandResponse findById(Long productBrandId) {
        return productBrandRepository
                .findById(productBrandId)
                .map(productBrandMapper::toProductBrandResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product brand by the provided id could not be found"));
    }

    @Override
    public ProductBrandResponse findByNameIgnoreCase(String name) {
        return productBrandRepository
                .findByNameIgnoreCase(name)
                .map(productBrandMapper::toProductBrandResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product brand by the provided name could not be found"));
    }
}
