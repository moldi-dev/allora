package com.moldi_sams.se_project.repository;

import com.moldi_sams.se_project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    Page<Product> findAllByStockGreaterThan(Long stock, Pageable pageable);
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);
}
