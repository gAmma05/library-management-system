package com.example.library_management_application.utils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.library_management_application.databases.entities.user.User;
import com.example.library_management_application.modules.auths.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class LmaSecurityFilter extends OncePerRequestFilter {
    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        LmaAuthentication lmaAuthentication = null;
        try {
            String accessToken = getAccessToken(req);
            Optional<User> user = authService.verifyAT(accessToken);
            lmaAuthentication = new LmaAuthentication(user.get());
        } catch (JWTVerificationException | NullPointerException e) {
            //Nothing happened because once you can't verify, just do nothing
        }
        context.setAuthentication(lmaAuthentication);
        filterChain.doFilter(req, resp);
    }

    private String getAccessToken(HttpServletRequest request) {
        String result = null;
        String accessToken = request.getHeader("Authorization");

        String[] parts = accessToken.split(" ");
        if (parts.length == 2 && parts[0].equals("Bearer")) {
            result = parts[1];
        }
        return result;
    }
}
