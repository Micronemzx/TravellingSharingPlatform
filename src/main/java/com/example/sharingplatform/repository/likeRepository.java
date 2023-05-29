package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.likeLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface likeRepository extends JpaRepository<likeLink,Long> {
    likeLink findByWorkIDAndUserID(long workID,long userID);
}
