package io.swagger.model;

import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountTest {

    private Account account;
    private AccountService accountService;
    private UserService userService;

    @BeforeEach
    public void init(){
        account = new Account(1L,"NL58INHO0123456789", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true );
        accountService = new AccountService();
    }
    @Test
    public void creatingAccountShouldNotBeNull(){
        Assertions.assertNotNull(account);
    }
    @Test
    public void getAccountTypeCurrentReturnAccountCurrent() {
        assertEquals(AccountType.CURRENT, account.getAccountType());
    }
    @Test
    public void setAccountTypeShouldUpdateAccountType() {
        account.setAccountType(AccountType.SAVINGS);
        assertEquals(AccountType.SAVINGS, account.getAccountType());
    }
    @Test
    public void getIbanShouldReturnIban() {
        assertEquals("NL58INHO0123456789", account.getIban());
    }

    @Test
    public void setIbanShouldUpdateIban() {
        account.setIban("NL58INHO0123456789");
        assertEquals("NL58INHO0123456789", account.getIban());
    }

    @Test
    public void setAbsoluteLimitShouldUpdateAbsoluteLimit() {
        account.setAbsoluteLimit(BigDecimal.valueOf(200));
        assertEquals(BigDecimal.valueOf(200), account.getAbsoluteLimit());
    }

    @Test
    public void getUserIdShouldReturnUserId() {
        assertEquals(1L, account.getUserId());
    }

}