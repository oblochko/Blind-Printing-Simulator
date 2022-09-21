package com.training.apparatus.data.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_WORKER, ROLE_BOSS, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
