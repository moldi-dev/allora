package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.Review;
import com.moldi_sams.se_project.response.ReviewResponse;
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
                review.getReviewDate()
        );
    }
}
