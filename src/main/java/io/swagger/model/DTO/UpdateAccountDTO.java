package io.swagger.model.DTO;

import io.swagger.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateAccountDTO {
    private Boolean open;
    private BigDecimal absoluteLimit;
    private BigDecimal balance;
    private AccountType accountType;

    public UpdateAccountDTO( Boolean open, BigDecimal absoluteLimit, BigDecimal balance, AccountType accountType) {

        this.open = open;
        this.absoluteLimit = absoluteLimit;
        this.balance = balance;
        this.accountType = accountType;
    }

    public UpdateAccountDTO() {
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public BigDecimal getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
