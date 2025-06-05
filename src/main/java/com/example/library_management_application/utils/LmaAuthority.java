package com.example.library_management_application.utils;

import org.springframework.security.core.GrantedAuthority;

//phan quyen
public class LmaAuthority implements GrantedAuthority {
    public static LmaAuthority of(Integer role) {
        return new LmaAuthority(role.toString());
    }

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    private LmaAuthority(String authority) {
        this.authority = authority;
    }
}
