package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.NotificationsDto;
import kg.amanturov.jortartip.model.Notifications;

import java.util.List;

public interface NotificationsService {

    List<NotificationsDto> findAll();

    void saveNotifications(NotificationsDto notificationsDto);

    List<NotificationsDto> findByNotificationsType(Long id);

    List<NotificationsDto> findByUserIdAndNotificationTypeId(Long userId, Long typeId);

    List<NotificationsDto> findByUserId(Long id);
    void deleteNotifications (Long id);
}
