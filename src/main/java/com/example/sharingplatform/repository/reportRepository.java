package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface reportRepository extends JpaRepository<complaint,Long> {
    complaint findByWorkIDAndUserID(long workID,long userID);
}
