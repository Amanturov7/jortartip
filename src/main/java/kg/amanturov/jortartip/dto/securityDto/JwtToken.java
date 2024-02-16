package kg.amanturov.jortartip.dto.securityDto;

import kg.amanturov.jortartip.model.User;
import kg.amanturov.jortartip.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {

    private String token;
    private UserService userService;

    public Optional<User> getUser() {

        return userService.getUserByToken(token);
    }
}
