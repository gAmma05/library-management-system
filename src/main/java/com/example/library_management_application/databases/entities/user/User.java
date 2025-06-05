package com.example.library_management_application.databases.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String fname;

    @Column(length = 100, nullable = false)
    private String lname;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private Integer role;

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = false)
    private String authProvider;

    @Column(nullable = false)
    private Boolean isEmailVerified;

    @Column
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @Column
    private Instant updatedAt;

    @Transient  //Ko cài vào database (khác @Column)
    public UserRole getUserRole() {
        return UserRole.fromValue(this.role);
    }

    public void setUserRole(UserRole userRole) {
        this.role = userRole != null ? userRole.getValue() : null;
    }

    public User(Integer userId, String email, String password, String fname, String lname, LocalDate dob, Integer role, String avatar) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.role = role;
        this.avatar = avatar;
    }

    public User(String email, String password, String fname, String lname, LocalDate dob, Integer role, String avatar) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.role = role;
        this.avatar = avatar;
    }

    public User(String email, String password, String fname, String lname, LocalDate dob, Integer role, String avatar, String authProvider, Boolean isEmailVerified, Instant createdAt) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.role = role;
        this.avatar = avatar;
        this.authProvider = authProvider;
        this.isEmailVerified = isEmailVerified;
    }

    public User(Integer userId, String email, String password, String fname, String lname, LocalDate dob, Integer role, String avatar, Instant createdAt, Instant updatedAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.role = role;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
