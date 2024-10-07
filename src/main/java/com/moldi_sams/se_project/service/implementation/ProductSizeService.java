package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.ProductSize;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.ProductSizeMapper;
import com.moldi_sams.se_project.repository.ProductSizeRepository;
import com.moldi_sams.se_project.response.ProductSizeResponse;
import com.moldi_sams.se_project.service.IProductSizeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductSizeService implements IProductSizeService {
    private final ProductSizeRepository productSizeRepository;
    private final ProductSizeMapper productSizeMapper;

    @Override
    public List<ProductSizeResponse> findAll() {
        List<ProductSize> productSizes = productSizeRepository.findAll();

        if (productSizes.isEmpty()) {
            throw new ResourceNotFoundException("No product sizes could be found");
        }

        return productSizes.stream().map(productSizeMapper::toProductSizeResponse).toList();
    }

    @Override
    public ProductSizeResponse findById(Long productSizeId) {
        return productSizeRepository
                .findById(productSizeId)
                .map(productSizeMapper::toProductSizeResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product size by the provided id couldn't be found"));
    }

    @Override
    public ProductSizeResponse findByNameIgnoreCase(String name) {
        return productSizeRepository
                .findByNameIgnoreCase(name)
                .map(productSizeMapper::toProductSizeResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product size by the provided name couldn't be found"));
    }
}
