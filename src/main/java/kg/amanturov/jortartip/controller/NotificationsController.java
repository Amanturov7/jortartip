package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.dto.NotificationsDto;
import kg.amanturov.jortartip.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/notifications")
public class NotificationsController {

    private final NotificationsService notificationsService;

    @Autowired
    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationsDto>> getAllNotifications() {
        List<NotificationsDto> notifications = notificationsService.findAll();
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveNotification(@RequestBody NotificationsDto notificationsDto) {
        notificationsService.saveNotifications(notificationsDto);
        return ResponseEntity.ok("Notification saved successfully");
    }

    @GetMapping("/notifications/type/{id}")
    public ResponseEntity<List<NotificationsDto>> getNotificationsByType(@PathVariable Long id) {
        List<NotificationsDto> notifications = notificationsService.findByNotificationsType(id);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/notifications/user/type")
    public ResponseEntity<List<NotificationsDto>> getNotificationsByUserAndType(@RequestParam(name = "userId") Long userId, Long typeId) {
        List<NotificationsDto> notifications = notificationsService.findByUserIdAndNotificationTypeId(userId,typeId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<NotificationsDto>> getNotificationsByUser(@PathVariable Long id) {
        List<NotificationsDto> notifications = notificationsService.findByUserId(id);
        return ResponseEntity.ok(notifications);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteSystemObject(@PathVariable Long id) {
        notificationsService.deleteNotifications(id);
    }

}
