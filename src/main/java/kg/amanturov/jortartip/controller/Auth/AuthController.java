package kg.amanturov.jortartip.controller.Auth;

import jakarta.validation.Valid;

import kg.amanturov.jortartip.dto.EmployeeSIgnUpDto;
import kg.amanturov.jortartip.dto.securityDto.JwtResponseDto;
import kg.amanturov.jortartip.dto.securityDto.JwtToken;
import kg.amanturov.jortartip.dto.securityDto.LoginRequest;
import kg.amanturov.jortartip.dto.securityDto.LoginResponse;
import kg.amanturov.jortartip.model.RefreshToken;
import kg.amanturov.jortartip.security.RefresherTokenService;
import kg.amanturov.jortartip.security.TokenProvider;
import kg.amanturov.jortartip.service.SignUpServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final SignUpServiceImpl signUpService;
    private final RefresherTokenService refresherTokenService;

    @PostMapping("/authenticate")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                JwtResponseDto dto = new JwtResponseDto();
                LoginResponse loginResponse = signUpService.findByUserName(loginRequest.getLogin());
                RefreshToken refreshToken = refresherTokenService.createRefreshToken(loginRequest.getLogin());
                dto.setAccessToken(authenticateAndGetToken(loginRequest.getLogin(), loginRequest.getPassword()));
                dto.setToken(refreshToken.getToken());
                dto.setRefresh(refreshToken.getExpiryDate());
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Пользователя с таким ником -" + loginRequest.getLogin() + " не существует", HttpStatus.BAD_REQUEST);
            }
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>("Ошибка аутентификации: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signUp(@RequestBody @Valid EmployeeSIgnUpDto employeeSIgnUpDtos) {
        signUpService.saveEmployee(employeeSIgnUpDtos);
    }

    private String authenticateAndGetToken(String username, String password) {
        return tokenProvider.GenerateToken(username);
    }
    @GetMapping("/test")
    private String test() {
        return "test";
    }

    @PostMapping("/refresh-token")
    public ResponseEntity refreshToken(@RequestBody JwtToken jwtToken){
        return new ResponseEntity<>(refresherTokenService.findByToken(jwtToken.getToken())
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = tokenProvider.GenerateToken(userInfo.getUsername());
                    return JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .refresh(refresherTokenService.findByToken(jwtToken.getToken()).get().getExpiryDate())
                            .token(jwtToken.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Токен не хранится в базе")), HttpStatus.OK);
    }
}
