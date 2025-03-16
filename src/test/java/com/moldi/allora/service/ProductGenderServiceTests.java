package com.moldi.allora.service;

import com.moldi.allora.entity.ProductGender;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ProductGenderMapper;
import com.moldi.allora.repository.ProductGenderRepository;
import com.moldi.allora.response.ProductGenderResponse;
import com.moldi.allora.service.implementation.ProductGenderService;
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
public class ProductGenderServiceTests {

    @Mock
    private ProductGenderRepository productGenderRepository;

    @Mock
    private ProductGenderMapper productGenderMapper;

    @InjectMocks
    private ProductGenderService productGenderService;

    private ProductGender productGender;
    private ProductGenderResponse productGenderResponse;

    @BeforeEach
    void setUp() {
        productGender = new ProductGender();
        productGender.setProductGenderId(1L);
        productGender.setName("Test Gender");

        productGenderResponse = new ProductGenderResponse(1L, "Test Gender");
    }

    @Test
    void findAll_ShouldReturnListOfProductGenderResponses_WhenProductGendersExist() {
        when(productGenderRepository.findAll()).thenReturn(Collections.singletonList(productGender));

        when(productGenderMapper.toProductGenderResponse(productGender)).thenReturn(productGenderResponse);

        List<ProductGenderResponse> result = productGenderService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productGenderResponse, result.get(0));

        verify(productGenderRepository, times(1)).findAll();
        verify(productGenderMapper, times(1)).toProductGenderResponse(productGender);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoProductGendersExist() {
        when(productGenderRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> productGenderService.findAll());

        verify(productGenderRepository, times(1)).findAll();
        verify(productGenderMapper, never()).toProductGenderResponse(any());
    }

    @Test
    void findById_ShouldReturnProductGenderResponse_WhenProductGenderExists() {
        when(productGenderRepository.findById(1L)).thenReturn(Optional.of(productGender));

        when(productGenderMapper.toProductGenderResponse(productGender)).thenReturn(productGenderResponse);

        ProductGenderResponse result = productGenderService.findById(1L);

        assertNotNull(result);
        assertEquals(productGenderResponse.productGenderId(), result.productGenderId());
        assertEquals(productGenderResponse.name(), result.name());

        verify(productGenderRepository, times(1)).findById(1L);
        verify(productGenderMapper, times(1)).toProductGenderResponse(productGender);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenProductGenderDoesNotExist() {
        when(productGenderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productGenderService.findById(1L));

        verify(productGenderRepository, times(1)).findById(1L);
        verify(productGenderMapper, never()).toProductGenderResponse(any());
    }

    @Test
    void findByNameIgnoreCase_ShouldReturnProductGenderResponse_WhenProductGenderExists() {
        when(productGenderRepository.findByNameIgnoreCase("Test Gender")).thenReturn(Optional.of(productGender));

        when(productGenderMapper.toProductGenderResponse(productGender)).thenReturn(productGenderResponse);

        ProductGenderResponse result = productGenderService.findByNameIgnoreCase("Test Gender");

        assertNotNull(result);
        assertEquals(productGenderResponse.productGenderId(), result.productGenderId());
        assertEquals(productGenderResponse.name(), result.name());

        verify(productGenderRepository, times(1)).findByNameIgnoreCase("Test Gender");
        verify(productGenderMapper, times(1)).toProductGenderResponse(productGender);
    }

    @Test
    void findByNameIgnoreCase_ShouldThrowResourceNotFoundException_WhenProductGenderDoesNotExist() {
        when(productGenderRepository.findByNameIgnoreCase("Non-existent Gender")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productGenderService.findByNameIgnoreCase("Non-existent Gender"));

        verify(productGenderRepository, times(1)).findByNameIgnoreCase("Non-existent Gender");
        verify(productGenderMapper, never()).toProductGenderResponse(any());
    }
}