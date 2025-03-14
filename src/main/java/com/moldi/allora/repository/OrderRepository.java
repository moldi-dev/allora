package com.moldi.allora.repository;

import com.moldi.allora.entity.Order;
import com.moldi.allora.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserPersonalInformationUserPersonalInformationId(Long userPersonalInformationId, Pageable pageable);
    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.orderLineProducts olp WHERE olp.product.productId = :productId")
    List<Order> findAllContainingProductId(@Param("productId") Long productId);

    Boolean existsByUserPersonalInformationUserPersonalInformationIdAndOrderStatusAndOrderLineProducts_Product_ProductId(Long userPersonalInformationId, OrderStatus orderStatus, Long productId);
}
