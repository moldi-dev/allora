package com.moldi.allora.service;

import com.moldi.allora.entity.Product;
import com.moldi.allora.entity.Review;
import com.moldi.allora.entity.User;
import com.moldi.allora.entity.UserPersonalInformation;
import com.moldi.allora.enumeration.OrderStatus;
import com.moldi.allora.exception.BadRequestException;
import com.moldi.allora.exception.ResourceAlreadyExistsException;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ReviewMapper;
import com.moldi.allora.repository.OrderRepository;
import com.moldi.allora.repository.ProductRepository;
import com.moldi.allora.repository.ReviewRepository;
import com.moldi.allora.request.user.ReviewRequest;
import com.moldi.allora.response.ReviewResponse;
import com.moldi.allora.service.implementation.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private ReviewResponse reviewResponse;
    private User user;
    private UserPersonalInformation personalInformation;
    private Product product;
    private ReviewRequest reviewRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        personalInformation = new UserPersonalInformation();
        personalInformation.setUserPersonalInformationId(1L);
        user.setPersonalInformation(personalInformation);

        product = new Product();
        product.setProductId(1L);

        review = Review.builder()
                .reviewId(1L)
                .product(product)
                .userPersonalInformation(personalInformation)
                .comment("Great product!")
                .rating(5)
                .build();

        reviewResponse = new ReviewResponse(1L, 1L, "Great product!", 5, "John", "Doe", LocalDateTime.now());

        reviewRequest = new ReviewRequest(1L, 5, "Great product!");
    }

    @Test
    void findAll_ShouldReturnPageOfReviewResponses_WhenReviewsExist() {
        Page<Review> reviewPage = new PageImpl<>(Collections.singletonList(review));
        when(reviewRepository.findAll(any(Pageable.class))).thenReturn(reviewPage);
        when(reviewMapper.toReviewResponse(review)).thenReturn(reviewResponse);

        Page<ReviewResponse> result = reviewService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(reviewResponse, result.getContent().get(0));

        verify(reviewRepository, times(1)).findAll(any(Pageable.class));
        verify(reviewMapper, times(1)).toReviewResponse(review);
    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoReviewsExist() {
        Page<Review> emptyPage = new PageImpl<>(Collections.emptyList());
        when(reviewRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> reviewService.findAll(Pageable.unpaged()));

        verify(reviewRepository, times(1)).findAll(any(Pageable.class));
        verify(reviewMapper, never()).toReviewResponse(any());
    }

    @Test
    void findAllByProductId_ShouldReturnPageOfReviewResponses_WhenReviewsExist() {
        Page<Review> reviewPage = new PageImpl<>(Collections.singletonList(review));
        when(reviewRepository.findAllByProductProductId(1L, Pageable.unpaged())).thenReturn(reviewPage);
        when(reviewMapper.toReviewResponse(review)).thenReturn(reviewResponse);

        Page<ReviewResponse> result = reviewService.findAllByProductId(1L, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(reviewResponse, result.getContent().get(0));

        verify(reviewRepository, times(1)).findAllByProductProductId(1L, Pageable.unpaged());
        verify(reviewMapper, times(1)).toReviewResponse(review);
    }

    @Test
    void findAllByProductId_ShouldThrowResourceNotFoundException_WhenNoReviewsExist() {
        Page<Review> emptyPage = new PageImpl<>(Collections.emptyList());
        when(reviewRepository.findAllByProductProductId(1L, Pageable.unpaged())).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> reviewService.findAllByProductId(1L, Pageable.unpaged()));

        verify(reviewRepository, times(1)).findAllByProductProductId(1L, Pageable.unpaged());
        verify(reviewMapper, never()).toReviewResponse(any());
    }

    @Test
    void findById_ShouldReturnReviewResponse_WhenReviewExists() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewMapper.toReviewResponse(review)).thenReturn(reviewResponse);

        ReviewResponse result = reviewService.findById(1L);

        assertNotNull(result);
        assertEquals(reviewResponse.reviewId(), result.reviewId());
        assertEquals(reviewResponse.comment(), result.comment());
        assertEquals(reviewResponse.rating(), result.rating());

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewMapper, times(1)).toReviewResponse(review);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenReviewDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.findById(1L));

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewMapper, never()).toReviewResponse(any());
    }

    @Test
    void save_ShouldReturnReviewResponse_WhenReviewIsSavedSuccessfully() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L)).thenReturn(false);
        when(orderRepository.existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L)).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toReviewResponse(review)).thenReturn(reviewResponse);

        ReviewResponse result = reviewService.save(authentication, reviewRequest);

        assertNotNull(result);
        assertEquals(reviewResponse.reviewId(), result.reviewId());
        assertEquals(reviewResponse.comment(), result.comment());
        assertEquals(reviewResponse.rating(), result.rating());

        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L);
        verify(orderRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(reviewMapper, times(1)).toReviewResponse(review);
    }

    @Test
    void save_ShouldThrowResourceAlreadyExistsException_WhenReviewAlreadyExists() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> reviewService.save(authentication, reviewRequest));

        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L);
        verify(orderRepository, never()).existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(any(), any(), any());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toReviewResponse(any());
    }

    @Test
    void save_ShouldThrowBadRequestException_WhenProductNotDelivered() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L)).thenReturn(false);
        when(orderRepository.existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> reviewService.save(authentication, reviewRequest));

        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L);
        verify(orderRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L);
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toReviewResponse(any());
    }

    @Test
    void updateById_ShouldReturnReviewResponse_WhenReviewIsUpdatedSuccessfully() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toReviewResponse(review)).thenReturn(reviewResponse);

        ReviewResponse result = reviewService.updateById(1L, reviewRequest);

        assertNotNull(result);
        assertEquals(reviewResponse.reviewId(), result.reviewId());
        assertEquals(reviewResponse.comment(), result.comment());
        assertEquals(reviewResponse.rating(), result.rating());

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(review);
        verify(reviewMapper, times(1)).toReviewResponse(review);
    }

    @Test
    void updateById_ShouldThrowResourceNotFoundException_WhenReviewDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.updateById(1L, reviewRequest));

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toReviewResponse(any());
    }

    @Test
    void canAuthenticatedUserPostReview_ShouldReturnTrue_WhenUserCanPostReview() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(reviewRepository.existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L)).thenReturn(false);
        when(orderRepository.existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L)).thenReturn(true);

        Boolean result = reviewService.canAuthenticatedUserPostReview(authentication, 1L);

        assertTrue(result);

        verify(reviewRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L);
        verify(orderRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L);
    }

    @Test
    void canAuthenticatedUserPostReview_ShouldReturnFalse_WhenUserCannotPostReview() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(reviewRepository.existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L)).thenReturn(true);
        when(orderRepository.existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L)).thenReturn(true);

        Boolean result = reviewService.canAuthenticatedUserPostReview(authentication, 1L);

        assertFalse(result);

        verify(reviewRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(1L, 1L);
        verify(orderRepository, times(1)).existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(1L, OrderStatus.DELIVERED, 1L);
    }

    @Test
    void deleteById_ShouldDeleteReview_WhenReviewExists() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteById(1L);

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void deleteById_ShouldThrowResourceNotFoundException_WhenReviewDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteById(1L));

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, never()).delete(any());
    }
}