package com.moldi.allora.service;

import com.moldi.allora.entity.ProductBrand;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductBrandMapper;
import com.moldi.allora.repository.ProductBrandRepository;
import com.moldi.allora.response.ProductBrandResponse;
import com.moldi.allora.service.implementation.ProductBrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductBrandServiceTests {

    @Mock
    private ProductBrandRepository productBrandRepository;

    @Mock
    private ProductBrandMapper productBrandMapper;

    @InjectMocks
    private ProductBrandService productBrandService;

    private ProductBrand productBrand;
    private ProductBrandResponse productBrandResponse;

    @BeforeEach
    void setUp() {
        productBrand = new ProductBrand();
        productBrand.setProductBrandId(1L);
        productBrand.setName("Test Brand");

        productBrandResponse = new ProductBrandResponse(1L, "Test Brand");
    }

    @Test
    void findAll_ShouldReturnPageOfProductBrandResponses_WhenProductBrandsExist() {
        Page<ProductBrand> productBrandPage = new PageImpl<>(Collections.singletonList(productBrand));
        when(productBrandRepository.findAll(any(Pageable.class))).thenReturn(productBrandPage);

        when(productBrandMapper.toProductBrandResponse(productBrand)).thenReturn(productBrandResponse);

        Page<ProductBrandResponse> result = productBrandService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(productBrandResponse, result.getContent().get(0));

        verify(productBrandRepository, times(1)).findAll(any(Pageable.class));
        verify(productBrandMapper, times(1)).toProductBrandResponse(productBrand);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoProductBrandsExist() {
        Page<ProductBrand> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productBrandRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> productBrandService.findAll(Pageable.unpaged()));

        verify(productBrandRepository, times(1)).findAll(any(Pageable.class));
        verify(productBrandMapper, never()).toProductBrandResponse(any());
    }

    @Test
    void findById_ShouldReturnProductBrandResponse_WhenProductBrandExists() {
        when(productBrandRepository.findById(1L)).thenReturn(Optional.of(productBrand));

        when(productBrandMapper.toProductBrandResponse(productBrand)).thenReturn(productBrandResponse);

        ProductBrandResponse result = productBrandService.findById(1L);

        assertNotNull(result);
        assertEquals(productBrandResponse.productBrandId(), result.productBrandId());
        assertEquals(productBrandResponse.name(), result.name());

        verify(productBrandRepository, times(1)).findById(1L);
        verify(productBrandMapper, times(1)).toProductBrandResponse(productBrand);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenProductBrandDoesNotExist() {
        when(productBrandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productBrandService.findById(1L));

        verify(productBrandRepository, times(1)).findById(1L);
        verify(productBrandMapper, never()).toProductBrandResponse(any());
    }

    @Test
    void findByNameIgnoreCase_ShouldReturnProductBrandResponse_WhenProductBrandExists() {
        when(productBrandRepository.findByNameIgnoreCase("Test Brand")).thenReturn(Optional.of(productBrand));

        when(productBrandMapper.toProductBrandResponse(productBrand)).thenReturn(productBrandResponse);

        ProductBrandResponse result = productBrandService.findByNameIgnoreCase("Test Brand");

        assertNotNull(result);
        assertEquals(productBrandResponse.productBrandId(), result.productBrandId());
        assertEquals(productBrandResponse.name(), result.name());

        verify(productBrandRepository, times(1)).findByNameIgnoreCase("Test Brand");
        verify(productBrandMapper, times(1)).toProductBrandResponse(productBrand);
    }

    @Test
    void findByNameIgnoreCase_ShouldThrowResourceNotFoundException_WhenProductBrandDoesNotExist() {
        when(productBrandRepository.findByNameIgnoreCase("Non-existent Brand")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productBrandService.findByNameIgnoreCase("Non-existent Brand"));

        verify(productBrandRepository, times(1)).findByNameIgnoreCase("Non-existent Brand");
        verify(productBrandMapper, never()).toProductBrandResponse(any());
    }
}