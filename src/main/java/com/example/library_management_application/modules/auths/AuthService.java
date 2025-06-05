package com.example.library_management_application.modules.auths;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.databases.repositories.users.UserRepository;
import com.example.library_management_application.modules.auths.dto.GoogleLoginDto;
import com.example.library_management_application.modules.auths.dto.LoginDTO;
import com.example.library_management_application.modules.users.UserController;
import com.example.library_management_application.modules.users.UserService;
import com.example.library_management_application.utils.configs.LmaConfiguration;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LmaConfiguration lmaConfiguration;

    @Autowired
    private UserController userController;

    //dung de sign token
    public String signJwtToken(User user) {
        Instant now = Instant.now();
        Algorithm algorithm = Algorithm.HMAC256(lmaConfiguration.getJwtSecret());
        return JWT.create()
                .withSubject(user.getUserId().toString())
                .withIssuer(lmaConfiguration.getJwtIssuer())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(30, ChronoUnit.DAYS))
                .sign(algorithm);
    }

    //cai nay se kiem tra user credential, neu correct -> sign token
    public String validateUser(LoginDTO loginDTO) {
        Optional<User> user = userRepository.findByEmail(loginDTO.getEmail());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        }

        User u = user.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDTO.getPassword(), u.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        }

        return signJwtToken(u);
    }

    public String loginThroughGoogle(GoogleLoginDto googleLoginDto) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(lmaConfiguration.getGoogleClientId()))
                .build();

        GoogleIdToken token = null;
        try {
            token = verifier.verify(googleLoginDto.getCredential());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ID Token is null");
        }
        GoogleIdToken.Payload payload = token.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        LocalDate defaultDob = LocalDate.of(2000, 1, 1);
        Instant now = Instant.now();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            System.out.println("User not found / email not registered");
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found / email not registered");
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setDob(defaultDob);
            newUser.setFname(firstName);
            newUser.setLname(lastName);
            newUser.setAvatar(pictureUrl);
            newUser.setAuthProvider("GOOGLE");
            newUser.setIsEmailVerified(payload.getEmailVerified());
            newUser.setRole(1);
            newUser.setCreatedAt(now);

            //cai nay se tu dong generate password 32 ky tu neu dang ky bang google (vi password field =! null)
            String randomPassword = generateSecureRandomPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(randomPassword);
            newUser.setPassword(hashedPassword);

            user = Optional.of(userRepository.save(newUser));

        } else {
            User existingUser = user.get();
            if (existingUser.getAuthProvider() != null && !existingUser.getAuthProvider().equalsIgnoreCase("GOOGLE")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User already registered with another provider");
            }
        }

        User u = user.get();

        System.out.println("User: " + googleLoginDto.getCredential().substring(0, 20) + "...");
        return signJwtToken(u);

    }

    private String generateSecureRandomPassword() {
        // Generate a random string of 32 characters
        byte[] randomBytes = new byte[24];
        new java.security.SecureRandom().nextBytes(randomBytes);
        return java.util.Base64.getEncoder().encodeToString(randomBytes);
    }


    //cai nay se tra ve user thong qua signed token
    public Optional<User> verifyAT(String accessToken) {
        Algorithm algorithm = Algorithm.HMAC256(lmaConfiguration.getJwtSecret());
        DecodedJWT jwt = (DecodedJWT) JWT.require(algorithm)
                .withIssuer(lmaConfiguration.getJwtIssuer())
                .build()
                .verify(accessToken);
        Integer userId = Integer.parseInt(jwt.getSubject());
        return userRepository.findById(userId);
    }

    public User getProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (User) context.getAuthentication().getPrincipal();
    }
}