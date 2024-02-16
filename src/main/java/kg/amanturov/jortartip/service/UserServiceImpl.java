package kg.amanturov.jortartip.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kg.amanturov.jortartip.Exceptions.UserNotFoundException;
import kg.amanturov.jortartip.dto.securityDto.JwtToken;
import kg.amanturov.jortartip.model.RefreshToken;
import kg.amanturov.jortartip.model.User;
import kg.amanturov.jortartip.repository.RefreshTokenRepository;
import kg.amanturov.jortartip.repository.UserRepository;
import kg.amanturov.jortartip.security.TokenProvider;
import org.springframework.stereotype.Service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    public UserServiceImpl(UserRepository userRepository, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public Optional<User> getUserByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(RefreshToken::getUserInfo);
    }



    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return false;
    }

    @Override
    public User validateAndGetUserByUsername(String username) {
        return getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username %s not found", username)));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
