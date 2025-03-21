package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.ProductGender;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductGenderMapper;
import com.moldi.allora.repository.ProductGenderRepository;
import com.moldi.allora.response.ProductGenderResponse;
import com.moldi.allora.service.IProductGenderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductGenderService implements IProductGenderService {
    private final ProductGenderRepository productGenderRepository;
    private final ProductGenderMapper productGenderMapper;

    @Override
    public List<ProductGenderResponse> findAll() {
        List<ProductGender> productGenders = productGenderRepository.findAll();

        if (productGenders.isEmpty()) {
            throw new ResourceNotFoundException("No product genders could be found");
        }

        return productGenders.stream().map(productGenderMapper::toProductGenderResponse).toList();
    }

    @Override
    public ProductGenderResponse findById(Long productGenderId) {
        return productGenderRepository
                .findById(productGenderId)
                .map(productGenderMapper::toProductGenderResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product gender by the provided id could not be found"));
    }

    @Override
    public ProductGenderResponse findByNameIgnoreCase(String name) {
        return productGenderRepository
                .findByNameIgnoreCase(name)
                .map(productGenderMapper::toProductGenderResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product gender by the provided name could not be found"));
    }
}
