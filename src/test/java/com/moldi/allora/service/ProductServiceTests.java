package com.moldi.allora.service;

import com.moldi.allora.entity.Product;
import com.moldi.allora.entity.ProductBrand;
import com.moldi.allora.entity.ProductCategory;
import com.moldi.allora.entity.ProductGender;
import com.moldi.allora.exception.ResourceAlreadyExistsException;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ImageMapper;
import com.moldi.allora.mapper.ProductMapper;
import com.moldi.allora.repository.*;
import com.moldi.allora.request.admin.ProductRequest;
import com.moldi.allora.request.user.ProductFilterRequest;
import com.moldi.allora.response.ProductResponse;
import com.moldi.allora.service.implementation.ImageService;
import com.moldi.allora.service.implementation.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private ProductSizeRepository productSizeRepository;

    @Mock
    private ProductBrandRepository productBrandRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductGenderRepository productGenderRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;
    private ProductFilterRequest productFilterRequest;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(100))
                .stock(10L)
                .sizes(new ArrayList<>())
                .brand(new ProductBrand())
                .category(new ProductCategory())
                .gender(new ProductGender())
                .images(new ArrayList<>())
                .build();

        productResponse = new ProductResponse(1L, "Test Product", "Test Description", BigDecimal.valueOf(100), 10L, Collections.emptyList(), null, null, null, Collections.emptyList());

        productRequest = new ProductRequest("Test Product", "Test Description", BigDecimal.valueOf(100), 10L, List.of("e1", "e2"), "Test Brand", "Test Gender", "Test Category", new ArrayList<MultipartFile>());

        productFilterRequest = new ProductFilterRequest("Test Product", List.of(1), List.of(1), List.of(1), List.of(1), BigDecimal.valueOf(50), BigDecimal.valueOf(150), "name-ascending");
    }

    @Test
    void findAll_ShouldReturnPageOfProductResponses_WhenProductsExist() {
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        Page<ProductResponse> result = productService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(productResponse, result.getContent().get(0));

        verify(productRepository, times(1)).findAll(any(Pageable.class));
        verify(productMapper, times(1)).toProductResponse(product);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoProductsExist() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> productService.findAll(Pageable.unpaged()));

        verify(productRepository, times(1)).findAll(any(Pageable.class));
        verify(productMapper, never()).toProductResponse(any());
    }

    @Test
    void findAllInStock_ShouldReturnPageOfProductResponses_WhenProductsExist() {
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAllByStockGreaterThan(0L, Pageable.unpaged())).thenReturn(productPage);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        Page<ProductResponse> result = productService.findAllInStock(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(productResponse, result.getContent().get(0));

        verify(productRepository, times(1)).findAllByStockGreaterThan(0L, Pageable.unpaged());
        verify(productMapper, times(1)).toProductResponse(product);
    }

    @Test
    void findAllInStock_ShouldThrowResourceNotFoundException_WhenNoProductsExist() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productRepository.findAllByStockGreaterThan(0L, Pageable.unpaged())).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> productService.findAllInStock(Pageable.unpaged()));

        verify(productRepository, times(1)).findAllByStockGreaterThan(0L, Pageable.unpaged());
        verify(productMapper, never()).toProductResponse(any());
    }

    @Test
    void findById_ShouldReturnProductResponse_WhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(productResponse.productId(), result.productId());
        assertEquals(productResponse.name(), result.name());
        assertEquals(productResponse.description(), result.description());
        assertEquals(productResponse.price(), result.price());
        assertEquals(productResponse.stock(), result.stock());

        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).toProductResponse(product);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(1L));

        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, never()).toProductResponse(any());
    }

    @Test
    void save_ShouldThrowResourceAlreadyExistsException_WhenProductNameExists() {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(product));

        assertThrows(ResourceAlreadyExistsException.class, () -> productService.save(productRequest));

        verify(productRepository, times(1)).findByName("Test Product");
        verify(productGenderRepository, never()).findByNameIgnoreCase(any());
        verify(productBrandRepository, never()).findByNameIgnoreCase(any());
        verify(productCategoryRepository, never()).findByNameIgnoreCase(any());
        verify(productSizeRepository, never()).findAllByNameInIgnoreCase(any());
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toProductResponse(any());
        verify(imageService, never()).save(any());
        verify(imageMapper, never()).toImage(any());
    }

    @Test
    void updateById_ShouldThrowResourceNotFoundException_WhenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateById(1L, productRequest));

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).findByName(any());
        verify(productGenderRepository, never()).findByNameIgnoreCase(any());
        verify(productBrandRepository, never()).findByNameIgnoreCase(any());
        verify(productCategoryRepository, never()).findByNameIgnoreCase(any());
        verify(productSizeRepository, never()).findAllByNameInIgnoreCase(any());
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toProductResponse(any());
        verify(imageService, never()).save(any());
        verify(imageMapper, never()).toImage(any());
    }

    @Test
    void deleteById_ShouldDeleteProduct_WhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.findAllContainingProductId(1L)).thenReturn(Collections.emptyList());
        when(reviewRepository.findAllByProductProductId(1L, null)).thenReturn(new PageImpl<>(Collections.emptyList()));

        productService.deleteById(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findAllContainingProductId(1L);
        verify(reviewRepository, times(1)).findAllByProductProductId(1L, null);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowResourceNotFoundException_WhenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(1L));

        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, never()).findAllContainingProductId(any());
        verify(reviewRepository, never()).findAllByProductProductId(any(), any());
        verify(productRepository, never()).deleteById(any());
    }
}