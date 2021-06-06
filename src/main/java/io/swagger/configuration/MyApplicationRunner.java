package io.swagger.configuration;

import io.swagger.model.*;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {


    //Add all repositories here and in constructor (or @Autowired)
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
        ////bank moet account hebben

        // Create users
        User user1 = new User(1L, "test", "test", LocalDate.of(2021,12,20), "lio@test.com","password", UserType.ROLE_EMPLOYEE, new BigDecimal("100.02"), new BigDecimal("200.02"), true);
        User user2 = new User(2L, "test", "test", LocalDate.of(2021,12,20), "lio@test2.com","password", UserType.ROLE_CUSTOMER, new BigDecimal("100.02"), new BigDecimal("200.02"), true);
        User user3 = new User(3L, "test", "test", LocalDate.of(2021,12,20), "cus","password", UserType.ROLE_CUSTOMER, new BigDecimal("100.02"), new BigDecimal("200.02"), false);
        User user4 = new User(4L, "test", "test", LocalDate.of(2021,12,20), "emp","password", UserType.ROLE_EMPLOYEE, new BigDecimal("100.02"), new BigDecimal("200.02"), false);

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);

        // Create an Account
        Account account1 = new Account(1L, "NL58INHO0123456789", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(10 ), true);
        Account account2 = new Account(2L, "NL58INHO0123456788", new BigDecimal(200 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(10 ), true);
        Account account3 = new Account(3L, "NL58INHO0123456701", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(10 ), true);
        //closed account
        Account account4 = new Account(4L, "NL58INHO0123456702", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(10 ), false);

        accountService.createAccount(account1);
        accountService.createAccount(account2);
        accountService.createAccount(account3);
        accountService.createAccount(account4);

        //Create transactions
        Transaction transaction1 = new Transaction(1, LocalDateTime.now(), "NL58INHO0123456789", "NL58INHO0123456788", new BigDecimal(100), "water bill");
        Transaction transaction2 = new Transaction(1, LocalDateTime.now(), "NL58INHO0123456789", "NL58INHO0123456701", new BigDecimal(100), "water bill");
        Transaction transaction3 = new Transaction(1, LocalDateTime.of(2020, 12, 28, 12, 00, 00), "NL58INHO0123456789", "NL58INHO0123456702", new BigDecimal(100), "water bill");
        Transaction transaction4 = new Transaction(1, LocalDateTime.of(2020, 12, 12, 12, 00, 00), "NL58INHO0123456788", "NL58INHO0123456701", new BigDecimal(100), "water bill");

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);
        transactionRepository.save(transaction4);
    }

}
