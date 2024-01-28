package kg.amanturov.jortartip.service;


import kg.amanturov.jortartip.Exceptions.DuplicatedUserInfoException;
import kg.amanturov.jortartip.dto.EmployeeSIgnUpDto;
import kg.amanturov.jortartip.model.User;
import kg.amanturov.jortartip.security.WebSecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    public SignUpServiceImpl(UserService userService,  PasswordEncoder passwordEncoder) {
        this.userService = userService;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveEmployee(List<EmployeeSIgnUpDto> dtos) {
        dtos.forEach(employeeDto -> {
            try {
                validateEmployeeDto(employeeDto);
                userService.saveUser(mapSignUpRequestToUser(employeeDto));
            } catch (DuplicatedUserInfoException e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
        });
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


