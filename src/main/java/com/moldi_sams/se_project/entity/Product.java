package com.moldi_sams.se_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    private Long stock;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ProductSize> sizes;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductBrand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductGender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductCategory category;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Image> images;
}
