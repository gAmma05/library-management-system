package com.example.library_management_application.modules.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.databases.repositories.users.UserRepository;
import com.example.library_management_application.modules.users.dto.CreateUserRequestDTO;
import com.example.library_management_application.modules.users.dto.UpdateUserRequestDTO;
import com.example.library_management_application.utils.configs.LmaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LmaConfiguration lmaConfiguration;

    public User createUser(CreateUserRequestDTO curDto) {
        return userRepository.save(curDto.toEntity());
    }

    public User updateUser(UpdateUserRequestDTO uurDto) {
        return userRepository.save(uurDto.toEntity());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersByRole(Integer role) {
        List<User> userGetByRole = new ArrayList<>();
        List<User> users = getAllUsers();
        for (User u : users) {
            if (Objects.equals(u.getRole(), role)) {
                userGetByRole.add(u);
            }
        }
        return userGetByRole;
    }

    public User getUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user.get();
    }

    public User deleteUser(Integer userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
        return user;
    }

    public Optional<User> getUserByAT(String accessToken) {
        Algorithm algorithm = Algorithm.HMAC256(lmaConfiguration.getJwtSecret());
        DecodedJWT jwt = (DecodedJWT) JWT.require(algorithm)
                .withIssuer(lmaConfiguration.getJwtIssuer())
                .build()
                .verify(accessToken);
        Integer userId = Integer.parseInt(jwt.getSubject());
        return userRepository.findById(userId);
    }


}
