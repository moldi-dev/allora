package com.moldi.allora.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineProduct extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineProductId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    private Long quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductSize productSize;
}
