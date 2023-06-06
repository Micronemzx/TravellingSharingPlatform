package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface notificationRepository extends JpaRepository<notification,Long> {
    @Query(value = "select * from notification n where n.notificationReceiver=?1 or n.notificationType=?2",nativeQuery = true)
    List<notification> findByNotificationReceiverOrNotificationType(long notificationReceiver,long notificationType);
}
