package kg.amanturov.jortartip.service;


import kg.amanturov.jortartip.Exceptions.DuplicatedUserInfoException;
import kg.amanturov.jortartip.dto.EmployeeSIgnUpDto;
import kg.amanturov.jortartip.dto.securityDto.LoginResponse;
import kg.amanturov.jortartip.model.User;
import kg.amanturov.jortartip.security.WebSecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    public SignUpServiceImpl(UserService userService,  PasswordEncoder passwordEncoder) {
        this.userService = userService;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveEmployee(EmployeeSIgnUpDto dtos) {
            try {
                validateEmployeeDto(dtos);
                userService.saveUser(mapSignUpRequestToUser(dtos));
            } catch (DuplicatedUserInfoException e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
    }



    @Override
    public LoginResponse findByUserName(String userName) {
        LoginResponse loginResponse = new LoginResponse();
        User user = userService.getUserByUsername(userName).get();
        loginResponse.setUserName(user.getUsername());
        return loginResponse;
    }

    private void validateEmployeeDto(EmployeeSIgnUpDto dto) {
        if (userService.hasUserWithUsername(dto.getLogin())) {
            throw new DuplicatedUserInfoException("Логин уже занят: " + dto.getLogin());
        }
        if (userService.hasUserWithEmail(dto.getEmail())) {
            throw new DuplicatedUserInfoException("Почта уже занята: " + dto.getEmail());
        }
    }
    private User mapSignUpRequestToUser(EmployeeSIgnUpDto signUpRequest) {
        User user = new User();

        user.setUsername(signUpRequest.getLogin());
        user.setInn(signUpRequest.getInn());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(WebSecurityConfig.USER);
        return user;
    }
}


