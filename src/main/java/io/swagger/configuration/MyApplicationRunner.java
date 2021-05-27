package io.swagger.configuration;

import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

@Component
public class MyApplicationRunner implements ApplicationRunner {


    private UserService userService;

    private AccountService accountService;

    public MyApplicationRunner(UserService userService ,AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Create one new user
        User user1 = new User(1L, "test", "test", LocalDate.of(2021,12,20), "lio","password", User.UserTypeEnum.EMPLOYEE, new BigDecimal("100.02"), new BigDecimal("200.02"), false);

        // Add the shops
        userService.createUser(user1);

        // Create an Account
        Account account1 = new Account(1L, "NL58INHO0123456789", new BigDecimal(9999.25 ), LocalDate.of(2021,05,27), Account.AccountTypeEnum.CURRENT, new BigDecimal(500 ), true);
        // Add account

        accountService.addAccount(account1);
    }
}
