package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface reportRepository extends JpaRepository<complaint,Long> {
    complaint findByWorkIDAndUserID(long workID,long userID);
    List<complaint> findByStatusOrderByIdAsc(int status);
}
