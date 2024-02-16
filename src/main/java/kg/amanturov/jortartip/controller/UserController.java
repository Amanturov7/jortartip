package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.Exceptions.UserNotFoundException;
import kg.amanturov.jortartip.model.User;
import kg.amanturov.jortartip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByToken(@RequestParam("token") String token) {
        Optional<User> userOptional = userService.getUserByToken(token);
        return userOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
