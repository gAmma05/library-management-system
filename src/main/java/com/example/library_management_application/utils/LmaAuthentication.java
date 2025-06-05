package com.example.library_management_application.utils;

import com.example.library_management_application.databases.entities.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class LmaAuthentication implements Authentication {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user != null ? List.of(LmaAuthority.of(user.getRole())) : List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return user != null;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    @Override
    public String getName() {
        return User.class.getName();
    }

    public LmaAuthentication(User user) {
        this.user = user;
    }

}
