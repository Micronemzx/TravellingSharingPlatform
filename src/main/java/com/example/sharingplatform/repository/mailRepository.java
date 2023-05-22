package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface mailRepository extends JpaRepository<email,Long> {
    email findByEmail(String email);
}
