package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<user,Long> {
    user findByEmail(String email);
    user findByUserID(long userID);

    user findByToken(String token);
}
