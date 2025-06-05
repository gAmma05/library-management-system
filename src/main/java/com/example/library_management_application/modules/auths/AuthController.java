package com.example.library_management_application.modules.auths;

import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.modules.auths.dto.GoogleLoginDto;
import com.example.library_management_application.modules.auths.dto.LoginDTO;
import com.example.library_management_application.modules.users.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    private ResponseEntity getProfile() {
        User user = authService.getProfile();
        return new ResponseEntity(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }

    @PostMapping("/googleLogin")
    public ResponseEntity<Map<String, String>> googleLogin(@RequestBody GoogleLoginDto googleLoginDto) {
        try {
            System.out.println("Received Google login request");
            String accessToken = authService.loginThroughGoogle(googleLoginDto);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e) {
            System.err.println("Error in login: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}
