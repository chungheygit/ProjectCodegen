package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

/**
 * Gets or Sets userType
 */
public enum UserType implements GrantedAuthority {
    ROLE_CUSTOMER("customer"),

    ROLE_EMPLOYEE("employee");

    private String value;

    UserType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static UserType fromValue(String text) {
        for (UserType b : UserType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public String getAuthority() {
        return value;
    }
}
