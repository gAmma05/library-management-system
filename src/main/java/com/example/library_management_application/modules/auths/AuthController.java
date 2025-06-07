package com.example.library_management_application.modules.auths;

import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.modules.auths.dto.GoogleLoginDto;
import com.example.library_management_application.modules.auths.dto.LoginDTO;
import com.example.library_management_application.modules.users.dto.UserResponseDTO;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity getProfile() {
        User user = authService.getProfile();
        return new ResponseEntity(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }

    @PostMapping("/googleLogin")
    public ResponseEntity<Map<String, String>> googleLogin(@Valid @RequestBody GoogleLoginDto googleLoginDto) {
        try {
            System.out.println("Received Google login request");
            String accessToken = authService.loginThroughGoogle(googleLoginDto);

//            Map<String, String> response = new HashMap<>();
//            response.put("accessToken", accessToken);

            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60 * 24 * 30)
                    .build();


            System.out.println("cookie: " + cookie.toString());

            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .build();
        } catch (Exception e) {
            System.err.println("Error in googleLogin: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        try {
            System.out.println("Received login request");
            String accessToken = authService.validateUser(loginDTO);

            if (accessToken == null) {
                return new ResponseEntity(new HashMap<String, Object>() {{
                    put("message", "Wrong email or password");
                }}, HttpStatus.UNAUTHORIZED);
            }

//            Map<String, String> response = new HashMap<>();
//            response.put("accessToken", accessToken);
            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60 * 24 * 30)
                    .build();


            System.out.println("cookie: " + cookie.toString());

            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .build();
        } catch (Exception e) {
            System.err.println("Error in login: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        System.out.println("Received logout request");
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/checkAT")
    public ResponseEntity checkAT(@CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/return_dashboard")
    public ResponseEntity returnDashboard(@CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null) {
            // If no cookie is present, redirect to login page
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        Optional<User> user = authService.verifyAT(accessToken);
        User u = null;
        if (user.isPresent()) {
            u = user.get();
        } else {
            System.out.println("User not found / access token is invalid");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        String result = authService.returnDashboard(u.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("redirectUrl", result));

    }
}
