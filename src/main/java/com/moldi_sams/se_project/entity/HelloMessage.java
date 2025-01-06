package com.moldi_sams.se_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "hello_messages")
public class HelloMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long helloMessageId;

    @Column(columnDefinition = "TEXT")
    private String content;
}
