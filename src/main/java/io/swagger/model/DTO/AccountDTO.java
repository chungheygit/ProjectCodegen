package io.swagger.model.DTO;


import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDTO {

    private Integer userId;
    private String iban;
    private LocalDate createdDate;
    private Boolean open;
    private BigDecimal absoluteLimit;



    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
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

