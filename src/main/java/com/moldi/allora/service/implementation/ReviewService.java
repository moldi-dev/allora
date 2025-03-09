package com.moldi.allora.service.implementation;

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
import com.moldi.allora.service.IReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<ReviewResponse> findAll(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews could be found");
        }

        return reviews.map(reviewMapper::toReviewResponse);
    }

    @Override
    public Page<ReviewResponse> findAllByProductId(Long productId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByProductProductId(productId, pageable);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews could be found");
        }

        return reviews.map(reviewMapper::toReviewResponse);
    }

    @Override
    public ReviewResponse findById(Long reviewId) {
        return reviewRepository
                .findById(reviewId)
                .map(reviewMapper::toReviewResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The review by the provided id couldn't be found"));
    }

    @Override
    public ReviewResponse save(Authentication authentication, ReviewRequest request) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        Product searchedProduct = productRepository
                .findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("The product by the provided id couldn't be found"));

        Boolean isAlreadyReviewed = reviewRepository
                .existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(personalInformation.getUserPersonalInformationId(), request.productId());

        if (isAlreadyReviewed) {
            throw new ResourceAlreadyExistsException("This user has already reviewed this product");
        }

        Boolean isBought = orderRepository
                .existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(personalInformation.getUserPersonalInformationId(), OrderStatus.DELIVERED, request.productId());

        if (!isBought) {
            throw new BadRequestException("The product hasn't been yet delivered to this user to be reviewable");
        }

        Review review = Review
                .builder()
                .product(searchedProduct)
                .userPersonalInformation(personalInformation)
                .comment(request.comment())
                .rating(request.rating())
                .build();

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse updateById(Long reviewId, ReviewRequest request) {
        Review searchedById = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("The review by the provided id couldn't be found"));

        searchedById.setComment(request.comment());
        searchedById.setRating(request.rating());

        return reviewMapper.toReviewResponse(reviewRepository.save(searchedById));
    }

    @Override
    public Boolean canAuthenticatedUserPostReview(Authentication authentication, Long productId) {
        User authenticatedUser = ((User) authentication.getPrincipal());
        UserPersonalInformation personalInformation = authenticatedUser.getPersonalInformation();

        Boolean reviewAlreadyExists = reviewRepository.existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(personalInformation.getUserPersonalInformationId(), productId);
        Boolean deliveredOrderExists = orderRepository.existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(personalInformation.getUserPersonalInformationId(), OrderStatus.DELIVERED, productId);

        return !reviewAlreadyExists && deliveredOrderExists;
    }

    @Override
    public void deleteById(Long reviewId) {
        Review searchedById = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("The review by the provided id couldn't be found"));

        reviewRepository.delete(searchedById);
    }
}
