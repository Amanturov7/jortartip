package kg.amanturov.jortartip.security;

import kg.amanturov.jortartip.model.RefreshToken;
import kg.amanturov.jortartip.repository.RefreshTokenRepository;
import kg.amanturov.jortartip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefresherTokenService {

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private final UserRepository userRepository;

    public RefresherTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusMinutes(6L))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(LocalDateTime.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Обновите токен");
        }
        return token;
    }
}
