package com.example.library_management_application.modules.users;

import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.modules.users.dto.CreateUserRequestDTO;
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

    @GetMapping("/list_accounts")
    private ResponseEntity getAllUsers() {
        List<UserResponseDTO> users = UserResponseDTO.fromEntities(userService.getAllUsers());
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    private ResponseEntity getUserById(@Valid @PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }
}
