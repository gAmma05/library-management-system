package com.example.library_management_application.modules.users.dto;

import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.modules.users.UserService;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class UpdateUserRequestDTO {

    private UserService userService;

    @NotNull(message = "User ID is required")
    private Integer userID;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")

    private String email;

    @NotBlank
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters and spaces")
    private String fname;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters and spaces")
    private String lname;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth must be in the past or present")
    private LocalDate dob;

    @NotBlank
    private String avatar;

    @NotBlank
    private Instant updatedAt;

    public User toEntity() { //update anything except id and password -> password will be modified in another stage
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User old = userService.getUserById(userID);

        User updatedUser = null;

        if (Objects.equals(old.getUserId(), userID)) {
            updatedUser = new User(old.getUserId(), email, old.getPassword(), fname, lname, dob, old.getRole(), avatar, old.getCreatedAt(), updatedAt);
        }
        return updatedUser;
    }
}
