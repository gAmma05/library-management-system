package com.example.library_management_application.modules.users;

import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.modules.auths.AuthService;
import com.example.library_management_application.modules.users.dto.CreateUserRequestDTO;
import com.example.library_management_application.modules.users.dto.UpdateUserRequestDTO;
import com.example.library_management_application.modules.users.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    private ResponseEntity createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        System.out.print("Received registration request: {}" + createUserRequestDTO.toEntity().toString());

        User user = userService.createUser(createUserRequestDTO);
        return new ResponseEntity(UserResponseDTO.fromEntity(user), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    private ResponseEntity updateUser(@Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        System.out.print("Received registration request: {}" + updateUserRequestDTO.toEntity().toString());
        User user = userService.updateUser(updateUserRequestDTO);
        return new ResponseEntity(UserResponseDTO.fromEntity(user), HttpStatus.OK);

    }

    @GetMapping("/list_accounts")
    private ResponseEntity getAllUsers() {
        List<UserResponseDTO> users = UserResponseDTO.fromEntities(userService.getAllUsers());
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping("/profile")
    private ResponseEntity getUserByToken(@CookieValue(name = "access_token") String accessToken, @PathVariable Integer userId) {
        User user = userService.getUserByAT(accessToken).orElseThrow(() -> new RuntimeException("Invalid access token"));
        return new ResponseEntity(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }
}
