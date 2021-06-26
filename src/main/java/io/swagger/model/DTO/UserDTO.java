package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.UserType;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UserDTO {
    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("lastName")
    private String lastName = null;

    @JsonProperty("dateOfBirth")
    private LocalDate dateOfBirth = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("userType")
    @Valid
    private UserType userTypeEnum;

    @JsonProperty("dayLimit")
    private BigDecimal dayLimit = null;

    @JsonProperty("transactionLimit")
    private BigDecimal transactionLimit = null;

    @JsonProperty("open")
    private Boolean open = null;

    public UserDTO(String firstName, String lastName, LocalDate dateOfBirth, String email, String password, @Valid UserType userTypeEnum, BigDecimal dayLimit, BigDecimal transactionLimit, Boolean open) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.userTypeEnum = userTypeEnum;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.open = open;
    }

    public UserDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserTypeEnum() {
        return userTypeEnum;
    }

    public void setUserTypeEnum(UserType userTypeEnum) {
        this.userTypeEnum = userTypeEnum;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public BigDecimal getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
