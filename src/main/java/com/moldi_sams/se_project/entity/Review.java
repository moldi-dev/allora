package com.moldi_sams.se_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reviews")
public class Review {
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

    private LocalDateTime reviewDate;
}
