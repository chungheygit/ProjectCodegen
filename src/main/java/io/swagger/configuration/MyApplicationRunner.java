package io.swagger.configuration;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private UserService userService;

    private AccountService accountService;

    //Add all repositories here and in constructor (or @Autowired)

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public MyApplicationRunner(UserService userService, AccountService accountService, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Create one new user
        User user1 = new User(1L, "test", "test", LocalDate.of(2021,12,20), "lio","password", User.UserTypeEnum.EMPLOYEE, new BigDecimal("100.02"), new BigDecimal("200.02"), false);

        // Add the shops
        userService.createUser(user1);

        // Create an Account
        Account account1 = new Account(1L, "NL58INHO0123456789", new BigDecimal(9997 ), java.time.LocalDate.of(2021,06,1), Account.AccountTypeEnum.CURRENT, new BigDecimal(500 ), true);
        Account account2 = new Account(2L, "NL58INHO0123456721", new BigDecimal(1000 ), java.time.LocalDate.of(2021,06,4), Account.AccountTypeEnum.CURRENT, new BigDecimal(500 ), true);
        Account account3 = new Account(3L, "NL58INHO0123456709", new BigDecimal(505 ), java.time.LocalDate.of(2021,06,4), Account.AccountTypeEnum.CURRENT, new BigDecimal(500 ), true);

        // Add account

        accountService.createAccount(account1);
        accountService.createAccount(account2);
        accountService.createAccount(account3);

        // create a transaction
        List<Transaction> transactions =
                Arrays.asList(
                        new Transaction("NL58INHO0255874139","NL58INHO0255885478",new BigDecimal(999.25 ),"")
                );
        transactions.forEach(transactionRepository::save);
    }
}
