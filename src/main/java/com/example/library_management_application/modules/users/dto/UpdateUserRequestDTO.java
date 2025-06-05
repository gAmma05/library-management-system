package com.example.library_management_application.modules.users.dto;

import com.example.library_management_application.databases.entities.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class UpdateUserRequestDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String fname;

    @NotBlank
    private String lname;

    @NotBlank
    private LocalDate dob;

    @NotBlank
    private String avatar;

    @NotBlank
    private Instant updatedAt;

    public User toEntity(User old) { //update anything except id and password -> password will be modified in another stage
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new User(old.getUserId(), email, old.getPassword(), fname, lname, dob, old.getRole(), avatar, old.getCreatedAt(), updatedAt);
    }
}
