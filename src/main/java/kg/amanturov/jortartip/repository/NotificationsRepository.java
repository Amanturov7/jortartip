package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.dto.NotificationsDto;
import kg.amanturov.jortartip.model.CommonReference;
import kg.amanturov.jortartip.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationsRepository  extends JpaRepository<Notifications, Long> {
    List<Notifications> findAllByNotificationType(CommonReference notificationType);
List<Notifications>   findByUserIdAndNotificationTypeId (Long userId, Long typeId);
List<Notifications>   findByUserId (Long id);
}
