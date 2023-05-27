package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface pictureRepository extends JpaRepository<picture,Long> {
    List<picture> findByWorkID(long workID);

    void deleteByWorkID(long workID);
}
