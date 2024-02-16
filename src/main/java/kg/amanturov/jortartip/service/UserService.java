package kg.amanturov.jortartip.service;



import kg.amanturov.jortartip.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {


    List<User> getUsers();

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByToken(String token);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);


    User validateAndGetUserByUsername(String username);

    User saveUser(User user);

    User findById(Long id);

    void deleteUser(User user);
}