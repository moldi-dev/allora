package com.moldi.allora.service;

import com.moldi.allora.entity.ProductSize;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductSizeMapper;
import com.moldi.allora.repository.ProductSizeRepository;
import com.moldi.allora.response.ProductSizeResponse;
import com.moldi.allora.service.implementation.ProductSizeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductSizeServiceTests {

    @Mock
    private ProductSizeRepository productSizeRepository;

    @Mock
    private ProductSizeMapper productSizeMapper;

    @InjectMocks
    private ProductSizeService productSizeService;

    private ProductSize productSize;
    private ProductSizeResponse productSizeResponse;

    @BeforeEach
    void setUp() {
        productSize = new ProductSize();
        productSize.setProductSizeId(1L);
        productSize.setName("Test Size");

        productSizeResponse = new ProductSizeResponse(1L, "Test Size");
    }

    @Test
    void findAll_ShouldReturnListOfProductSizeResponses_WhenProductSizesExist() {
        when(productSizeRepository.findAll()).thenReturn(Collections.singletonList(productSize));
        when(productSizeMapper.toProductSizeResponse(productSize)).thenReturn(productSizeResponse);

        List<ProductSizeResponse> result = productSizeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productSizeResponse, result.get(0));

        verify(productSizeRepository, times(1)).findAll();
        verify(productSizeMapper, times(1)).toProductSizeResponse(productSize);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoProductSizesExist() {
        when(productSizeRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> productSizeService.findAll());

        verify(productSizeRepository, times(1)).findAll();
        verify(productSizeMapper, never()).toProductSizeResponse(any());
    }

    @Test
    void findById_ShouldReturnProductSizeResponse_WhenProductSizeExists() {
        when(productSizeRepository.findById(1L)).thenReturn(Optional.of(productSize));
        when(productSizeMapper.toProductSizeResponse(productSize)).thenReturn(productSizeResponse);

        ProductSizeResponse result = productSizeService.findById(1L);

        assertNotNull(result);
        assertEquals(productSizeResponse.productSizeId(), result.productSizeId());
        assertEquals(productSizeResponse.name(), result.name());

        verify(productSizeRepository, times(1)).findById(1L);
        verify(productSizeMapper, times(1)).toProductSizeResponse(productSize);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenProductSizeDoesNotExist() {
        when(productSizeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productSizeService.findById(1L));

        verify(productSizeRepository, times(1)).findById(1L);
        verify(productSizeMapper, never()).toProductSizeResponse(any());
    }

    @Test
    void findByNameIgnoreCase_ShouldReturnProductSizeResponse_WhenProductSizeExists() {
        when(productSizeRepository.findByNameIgnoreCase("Test Size")).thenReturn(Optional.of(productSize));
        when(productSizeMapper.toProductSizeResponse(productSize)).thenReturn(productSizeResponse);

        ProductSizeResponse result = productSizeService.findByNameIgnoreCase("Test Size");

        assertNotNull(result);
        assertEquals(productSizeResponse.productSizeId(), result.productSizeId());
        assertEquals(productSizeResponse.name(), result.name());

        verify(productSizeRepository, times(1)).findByNameIgnoreCase("Test Size");
        verify(productSizeMapper, times(1)).toProductSizeResponse(productSize);
    }

    @Test
    void findByNameIgnoreCase_ShouldThrowResourceNotFoundException_WhenProductSizeDoesNotExist() {
        when(productSizeRepository.findByNameIgnoreCase("Non-existent Size")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productSizeService.findByNameIgnoreCase("Non-existent Size"));

        verify(productSizeRepository, times(1)).findByNameIgnoreCase("Non-existent Size");
        verify(productSizeMapper, never()).toProductSizeResponse(any());
    }
}