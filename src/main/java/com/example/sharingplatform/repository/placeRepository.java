package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface placeRepository  extends JpaRepository<place,Long> {

}
