package com.example.library_management_application.utils.configs;

import com.example.library_management_application.utils.LmaAuthority;
import com.example.library_management_application.utils.LmaSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class LmaSecurityConfiguration {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public LmaSecurityFilter lmaSecurityFilter() {
        return new LmaSecurityFilter();
    }

    @Bean
    public SecurityFilterChain filterChainWithLmaSecurity(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/profile")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**")
                        .hasAnyAuthority(LmaAuthority.of(0).getAuthority(), LmaAuthority.of(1).getAuthority())
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers("/api/library/**")
                        .hasAnyAuthority(LmaAuthority.of(0).getAuthority(), LmaAuthority.of(1).getAuthority())
                        .requestMatchers("/api/users/list_accounts")
                        .hasAnyAuthority(LmaAuthority.of(0).getAuthority())
                        .anyRequest()
                        .denyAll()
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(lmaSecurityFilter(), AuthorizationFilter.class);
        return http.build();
    }
}
