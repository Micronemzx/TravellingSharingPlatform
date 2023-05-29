package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface notificationRepository extends JpaRepository<notification,Long> {

}
