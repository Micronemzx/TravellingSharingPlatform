package com.example.sharingplatform.service.serviceImpl;

import com.example.sharingplatform.entity.notification;
import com.example.sharingplatform.repository.notificationRepository;
import com.example.sharingplatform.service.NotificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    notificationRepository notificationRep;
    @Override
    public void sendNotification(long userID, String msg, int type) {
        notification entity = new notification();
        entity.setNotificationContent(msg);
        entity.setNotificationReceiver(userID);
        entity.setNotificationType(type);
        entity.setNotificationTime(new Date());
        notificationRep.save(entity);
    }

    @Override
    public List<notification> getNotification(long userId)
    {
        return notificationRep.findByNotificationReceiverOrNotificationType(userId,0);
    }
}
