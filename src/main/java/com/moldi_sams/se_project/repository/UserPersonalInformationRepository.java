package com.moldi_sams.se_project.repository;

import com.moldi_sams.se_project.entity.UserPersonalInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPersonalInformationRepository extends JpaRepository<UserPersonalInformation, Long> {
    Page<UserPersonalInformation> findAllByFirstNameAndLastNameLikeIgnoreCase(String firstName, String lastName, Pageable pageable);
}
