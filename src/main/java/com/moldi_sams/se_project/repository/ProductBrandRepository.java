package com.moldi_sams.se_project.repository;

import com.moldi_sams.se_project.entity.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {
    Optional<ProductBrand> findByNameIgnoreCase(String name);
}
