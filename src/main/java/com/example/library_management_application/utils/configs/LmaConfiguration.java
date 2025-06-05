package com.example.library_management_application.utils.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(LmaSecurityConfiguration.class)
@Data
public class LmaConfiguration {
    @Value("${lma.jwt.secret}")
    private String jwtSecret;

    @Value("${lma.jwt.issuer}")
    private String jwtIssuer;

    @Value("${lma.google.client.id}")
    private String googleClientId;
}
