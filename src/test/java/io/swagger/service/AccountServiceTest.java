package io.swagger.service;

import io.swagger.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AccountServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;



    @Test
    public void testGetAllAccount() throws Exception {
        List<Account> accountResponses = accountService.GetAllAccounts();
        assertTrue(accountResponses.size() > 0);
    }


}