package com.moldi.allora.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reviews")
public class Review extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Integer rating;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserPersonalInformation userPersonalInformation;
}
