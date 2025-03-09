package com.moldi.allora.repository;

import com.moldi.allora.entity.ProductGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductGenderRepository extends JpaRepository<ProductGender, Long> {
    Optional<ProductGender> findByNameIgnoreCase(String name);
}
