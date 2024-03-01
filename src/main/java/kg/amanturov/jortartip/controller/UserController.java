package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.model.User;
import kg.amanturov.jortartip.service.FileStorageService;
import kg.amanturov.jortartip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;


    @Autowired
    public UserController(UserService userService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByToken(@RequestParam("token") String token) {
        Optional<User> userOptional = userService.getUserByToken(token);
        return userOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            fileStorageService.deleteAvatar(userId);

            fileStorageService.saveAvatar(file, userId);

            return ResponseEntity.ok().body("Аватар успешно загружен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при загрузке аватара: " + e.getMessage());
        }
    }
    @PutMapping("/{userId}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            fileStorageService.updateAvatar(file, userId);
            return ResponseEntity.ok().body("Аватар успешно обновлен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении аватара: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/avatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long userId) {
        try {
            fileStorageService.deleteAvatar(userId);
            return ResponseEntity.ok().body("Аватар успешно удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении аватара: " + e.getMessage());
        }
    }

}
