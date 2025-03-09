package com.moldi.allora.repository;

import com.moldi.allora.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Boolean existsByUserPersonalInformationUserPersonalInformationIdAndProductProductId(Long userPersonalInformationId, Long productId);
    Page<Review> findAllByProductProductId(Long productId, Pageable pageable);
    Page<Review> findAllByUserPersonalInformationUserPersonalInformationId(Long userPersonalInformationId, Pageable pageable);
}
