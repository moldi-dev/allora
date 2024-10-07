package com.moldi_sams.se_project.repository;

import com.moldi_sams.se_project.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    Optional<ProductSize> findByNameIgnoreCase(String name);
    List<ProductSize> findAllByNameInIgnoreCase(List<String> names);
}
