package kg.amanturov.jortartip.controller.Auth;

import jakarta.validation.Valid;
import kg.amanturov.jortartip.dto.EmployeeSIgnUpDto;
import kg.amanturov.jortartip.dto.securityDto.AuthResponse;
import kg.amanturov.jortartip.dto.securityDto.LoginRequest;
import kg.amanturov.jortartip.security.TokenProvider;
import kg.amanturov.jortartip.service.SignUpServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final SignUpServiceImpl signUpService;

    @PostMapping("/authenticate")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        String token = authenticateAndGetToken(loginRequest.getLogin(), loginRequest.getPassword());
        return new AuthResponse(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signUp(@RequestBody @Valid List<EmployeeSIgnUpDto> employeeSIgnUpDtos) {
        signUpService.saveEmployee(employeeSIgnUpDtos);
    }

    private String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }
}
