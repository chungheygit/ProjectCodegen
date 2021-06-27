package io.swagger.model.DTO;


import io.swagger.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDTO {

    private Long userId;
    private Boolean open;
    private BigDecimal absoluteLimit;
    private BigDecimal balance;
    private AccountType accountType;

    public AccountDTO(Long userId, Boolean open, BigDecimal absoluteLimit, BigDecimal balance, AccountType accountType) {
        this.userId = userId;
        this.open = open;
        this.absoluteLimit = absoluteLimit;
        this.balance = balance;
        this.accountType = accountType;
    }

    public AccountDTO() {
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

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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
}

