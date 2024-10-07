package com.moldi_sams.se_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_genders")
public class ProductGender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productGenderId;

    private String name;
}
