package io.swagger.model.DTO;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class TransactionDTO {

    private String sender;
    private String receiver;
    private BigDecimal amount;
    private String description;

    public TransactionDTO(String sender, String receiver, BigDecimal amount, String description) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.description = description;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
