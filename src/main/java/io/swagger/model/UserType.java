package io.swagger.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    ROLE_Customer, ROLE_Employee;

    public String getAuthority() {
        return name();
    }
}
