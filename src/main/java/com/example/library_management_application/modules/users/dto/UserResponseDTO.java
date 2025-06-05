package com.example.library_management_application.modules.users.dto;

import com.example.library_management_application.databases.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Integer userID;
    private String email;
    private String fname;
    private String lname;
    private LocalDate dob;
    private Integer role;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getFname(),
                user.getLname(),
                user.getDob(),
                user.getRole(),
                user.getAvatar(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static List<UserResponseDTO> fromEntities(List<User> users) {
        return users.stream().map(UserResponseDTO::fromEntity).toList();
    }
}
