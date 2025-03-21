package com.moldi.allora.service;

import com.moldi.allora.request.user.ReviewRequest;
import com.moldi.allora.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface IReviewService {
    Page<ReviewResponse> findAll(Pageable pageable);
    Page<ReviewResponse> findAllByProductId(Long productId, Pageable pageable);
    ReviewResponse findById(Long reviewId);
    ReviewResponse save(Authentication authentication, ReviewRequest request);
    ReviewResponse updateById(Long reviewId, ReviewRequest request);
    Boolean canAuthenticatedUserPostReview(Authentication authentication, Long productId);
    void deleteById(Long reviewId);
}
