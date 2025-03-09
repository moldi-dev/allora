package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.ProductSize;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductSizeMapper;
import com.moldi.allora.repository.ProductSizeRepository;
import com.moldi.allora.response.ProductSizeResponse;
import com.moldi.allora.service.IProductSizeService;
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
