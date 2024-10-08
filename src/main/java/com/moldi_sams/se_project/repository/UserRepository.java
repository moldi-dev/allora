package com.moldi_sams.se_project.repository;

import com.moldi_sams.se_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmailIgnoreCase(String email);

    Page<User> findAllByEmailLikeIgnoreCase(String email, Pageable pageable);
    Page<User> findAllByUsernameLikeIgnoreCase(String username, Pageable pageable);
}
