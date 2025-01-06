package com.moldi_sams.se_project.repository;

import com.moldi_sams.se_project.entity.HelloMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloMessageRepository extends JpaRepository<HelloMessage, Long> {
}
