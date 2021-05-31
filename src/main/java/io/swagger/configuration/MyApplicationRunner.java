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
        Account account1 = new Account(1L, "NL58INHO0123456789", new BigDecimal(9999.25 ), LocalDateTime.of(2021,05,27,12,00), Account.AccountTypeEnum.CURRENT, new BigDecimal(500 ), true);
        // Add account

        accountService.createAccount(account1);
        accountService.createAccount(account2);

        Transaction transaction1 = new Transaction(1, LocalDateTime.now(), "NL58INHO0123456789", "NL58INHO0123456788", new BigDecimal(200), "water bill");
        Transaction transaction2 = new Transaction(1, LocalDateTime.now(), "NL58INHO0123456789", "NL58INHO0123456701", new BigDecimal(200), "water bill");
        Transaction transaction3 = new Transaction(1, LocalDateTime.of(2020, 12, 28, 12, 00, 00), "NL58INHO0123456789", "NL58INHO0123456702", new BigDecimal(200), "water bill");
        Transaction transaction4 = new Transaction(1, LocalDateTime.of(2020, 12, 12, 12, 00, 00), "NL58INHO0123456788", "NL58INHO0123456701", new BigDecimal(200), "water bill");

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);
        transactionRepository.save(transaction4);
    }
}
