package com.example.library_management_application.modules.users.dto;

import com.example.library_management_application.databases.entities.user.User;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class CreateUserRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
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

    private String avatar;

    @NotBlank(message = "Authentication provider is required")
    private String authProvider;

    @NotNull(message = "Email verification status is required")
    private Boolean isEmailVerified;


    private Instant createdAt;

    public User toEntity() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new User(email, encoder.encode(password), fname, lname, dob, 1, avatar, authProvider, isEmailVerified, createdAt);
    }
}
