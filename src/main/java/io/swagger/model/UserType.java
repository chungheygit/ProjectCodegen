package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

/**
 * Gets or Sets userType
 */
public enum UserType implements GrantedAuthority {
    ROLE_CUSTOMER,
    ROLE_EMPLOYEE;

    @Override
    public String getAuthority() {
        return name();
    }
}
