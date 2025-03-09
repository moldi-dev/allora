package com.moldi.allora.mapper;

import com.moldi.allora.entity.Review;
import com.moldi.allora.response.ReviewResponse;
import org.springframework.stereotype.Service;

@Service
public class ReviewMapper {
    public ReviewResponse toReviewResponse(Review review) {
        return new ReviewResponse(
                review.getReviewId(),
                review.getProduct().getProductId(),
                review.getComment(),
                review.getRating(),
                review.getUserPersonalInformation().getFirstName(),
                review.getUserPersonalInformation().getLastName(),
                review.getCreatedDate()
        );
    }
}
