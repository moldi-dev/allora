package com.moldi_sams.se_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_personal_informations")
public class UserPersonalInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPersonalInformationId;

    private String firstName;

    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String address;
}
