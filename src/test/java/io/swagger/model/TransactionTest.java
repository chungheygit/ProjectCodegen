package io.swagger.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionTest {

    private Transaction transaction;
    @BeforeEach
    public void init(){
        transaction = new Transaction(1L, LocalDateTime.of(2020, 12, 28, 12, 00, 00), "NL58INHO0123456789", "NL58INHO0123456788", new BigDecimal(100), "test");
    }

    @Test
    public void creatingTransactionShouldNotBeNull(){
        Assertions.assertNotNull(transaction);
    }

    @Test
    public void getTransactionIdShouldReturnTransactionId(){
        transaction.setId(1);
        assertEquals(1, transaction.getId());
    }

//    @Test
//    public void getUserPerformingShouldReturnPerformingUser() {
//        assertEquals(user, transaction.getUserPerforming());
//    }
//
//    @Test
//    public void getAccountFromShouldReturnAccount() {
//        assertEquals(accountFrom, transaction.getAccountFrom());
//    }

    @Test
    public void getAmountShouldReturnAmount() {
        assertEquals(BigDecimal.valueOf(100), transaction.getAmount());
    }

    @Test
    public void setAmountShouldUpdateAmount() {
        transaction.setAmount(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), transaction.getAmount());
    }

    @Test
    public void setTransactionDateTimeShouldUpdateTransactionDateTime() {
        transaction.setTimestamp(LocalDateTime.parse("2020-12-03T10:15:30"));
        assertEquals(LocalDateTime.parse("2020-12-03T10:15:30"), transaction.getTimestamp());
    }

    @Test
    public void validateTransactionToSameAccountShouldThrowIllegalArgumentException() {
        transaction.setReceiver("NL58INHO0123456789");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, transaction::validate);
        assertThat(e).hasMessage("Cannot transfer to same account");
    }
//    @Test
//    public void validateTransactionToOtherUserSavingsShouldThrowIllegalArgumentException() {
//        accountFrom.setAccountType(AccountType.SAVINGS);
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, transaction::validate);
//        assertThat(e).hasMessage("Can only transfer to or from savings account of the same user");
//    }
//
//    @Test
//    public void validateTransactionToSameUserSavingsShouldNotThrowIllegalArgumentException() {
//        accountFrom.userId(2L);
//        accountTo.userId(2L);
//        accountTo.setAccountType(AccountType.SAVINGS);
//
//        assertDoesNotThrow(transaction::validate);
//    }
//
//    @Test
//    public void transactionExceedingDayLimitShouldReturnIllegalArgumentException() {
//        accountFrom.setDayLimit(BigDecimal.valueOf(100.00));
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, transaction::validate);
//        assertThat(e).hasMessage("Transaction would exceed daily limit.");
//    }
//
//    @Test
//    public void transactionExceedingTransactionLimitShouldReturnIllegalArgumentException() {
//        accountFrom.setTransactionLimit(BigDecimal.valueOf(100.00));
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, transaction::validate);
//        assertThat(e).hasMessage("Transaction amount exceeds transaction limit for account.");
//    }

    @Test
    public void transactionExceedingAbsoluteLimitShouldReturnIllegalArgumentException() {
        transaction.setAmount(BigDecimal.valueOf(3000.00));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, transaction::validate);
        assertThat(e).hasMessage("Transaction would lower balance below absolute account limit.");
    }

}