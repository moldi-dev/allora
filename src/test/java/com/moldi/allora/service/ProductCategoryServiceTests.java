package com.moldi.allora.service;

import com.moldi.allora.entity.ProductCategory;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductCategoryMapper;
import com.moldi.allora.repository.ProductCategoryRepository;
import com.moldi.allora.response.ProductCategoryResponse;
import com.moldi.allora.service.implementation.ProductCategoryService;
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
public class ProductCategoryServiceTests {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCategoryMapper productCategoryMapper;

    @InjectMocks
    private ProductCategoryService productCategoryService;

    private ProductCategory productCategory;
    private ProductCategoryResponse productCategoryResponse;

    @BeforeEach
    void setUp() {
        productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        productCategory.setName("Test Category");

        productCategoryResponse = new ProductCategoryResponse(1L, "Test Category");
    }

    @Test
    void findAll_ShouldReturnPageOfProductCategoryResponses_WhenProductCategoriesExist() {
        Page<ProductCategory> productCategoryPage = new PageImpl<>(Collections.singletonList(productCategory));
        when(productCategoryRepository.findAll(any(Pageable.class))).thenReturn(productCategoryPage);

        when(productCategoryMapper.toProductCategoryResponse(productCategory)).thenReturn(productCategoryResponse);

        Page<ProductCategoryResponse> result = productCategoryService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(productCategoryResponse, result.getContent().get(0));

        verify(productCategoryRepository, times(1)).findAll(any(Pageable.class));
        verify(productCategoryMapper, times(1)).toProductCategoryResponse(productCategory);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoProductCategoriesExist() {
        Page<ProductCategory> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productCategoryRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> productCategoryService.findAll(Pageable.unpaged()));

        verify(productCategoryRepository, times(1)).findAll(any(Pageable.class));
        verify(productCategoryMapper, never()).toProductCategoryResponse(any());
    }

    @Test
    void findById_ShouldReturnProductCategoryResponse_WhenProductCategoryExists() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(productCategory));

        when(productCategoryMapper.toProductCategoryResponse(productCategory)).thenReturn(productCategoryResponse);

        ProductCategoryResponse result = productCategoryService.findById(1L);

        assertNotNull(result);
        assertEquals(productCategoryResponse.productCategoryId(), result.productCategoryId());
        assertEquals(productCategoryResponse.name(), result.name());

        verify(productCategoryRepository, times(1)).findById(1L);
        verify(productCategoryMapper, times(1)).toProductCategoryResponse(productCategory);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenProductCategoryDoesNotExist() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productCategoryService.findById(1L));

        verify(productCategoryRepository, times(1)).findById(1L);
        verify(productCategoryMapper, never()).toProductCategoryResponse(any());
    }

    @Test
    void findByNameIgnoreCase_ShouldReturnProductCategoryResponse_WhenProductCategoryExists() {
        when(productCategoryRepository.findByNameIgnoreCase("Test Category")).thenReturn(Optional.of(productCategory));

        when(productCategoryMapper.toProductCategoryResponse(productCategory)).thenReturn(productCategoryResponse);

        ProductCategoryResponse result = productCategoryService.findByNameIgnoreCase("Test Category");

        assertNotNull(result);
        assertEquals(productCategoryResponse.productCategoryId(), result.productCategoryId());
        assertEquals(productCategoryResponse.name(), result.name());

        verify(productCategoryRepository, times(1)).findByNameIgnoreCase("Test Category");
        verify(productCategoryMapper, times(1)).toProductCategoryResponse(productCategory);
    }

    @Test
    void findByNameIgnoreCase_ShouldThrowResourceNotFoundException_WhenProductCategoryDoesNotExist() {
        when(productCategoryRepository.findByNameIgnoreCase("Non-existent Category")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productCategoryService.findByNameIgnoreCase("Non-existent Category"));

        verify(productCategoryRepository, times(1)).findByNameIgnoreCase("Non-existent Category");
        verify(productCategoryMapper, never()).toProductCategoryResponse(any());
    }
}