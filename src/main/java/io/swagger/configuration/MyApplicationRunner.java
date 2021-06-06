package io.swagger.configuration;

import io.swagger.model.*;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;
import java.time.LocalDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {


    //Add all repositories here and in constructor (or @Autowired)
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ////bank moet account hebben

        // Create users
        List<User> users =
                Arrays.asList(
                        new User("Bruno", "Fernandes", LocalDate.of(2021,1,25), "lio@test.com","password", UserType.ROLE_EMPLOYEE, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Frenkie", "De Jong", LocalDate.of(2021,4,20), "lio@test2.com","password", UserType.ROLE_CUSTOMER, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Kevin", "De Bruyne", LocalDate.of(2021,6,1), "cus","password", UserType.ROLE_CUSTOMER, new BigDecimal("1000.02"), new BigDecimal("250.02"), false),
                        new User("N'Golo", "Kanté", LocalDate.of(2021,3,18), "emp","password", UserType.ROLE_EMPLOYEE, new BigDecimal("1000.02"), new BigDecimal("250.02"), false)
                );
        users.forEach(userService ::createUser);

        // Create accounts
        List<Account> accounts =
                Arrays.asList(
                        new Account(users.get(0).getId(), "NL58INHO0123456789", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(users.get(1).getId(), "NL58INHO0123456788", new BigDecimal(200 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(users.get(2).getId(), "NL58INHO0123456701", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(users.get(3).getId(), "NL58INHO0123456702", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), false)
                );

        accounts.forEach(accountRepository::save);

        // Create transactions
        List<Transaction> transactions =
                Arrays.asList(
                        new Transaction(users.get(0).getId(), LocalDateTime.now(), "NL58INHO0123456789", "NL58INHO0123456788", new BigDecimal(150), "water bill"),
                        new Transaction(users.get(1).getId(), LocalDateTime.now(), "NL58INHO0123456788", "NL58INHO0123456701", new BigDecimal(230), "taxes"),
                        new Transaction(users.get(2).getId(), LocalDateTime.of(2020, 12, 28, 12, 00, 00), "NL58INHO0123456701", "NL58INHO0123456702", new BigDecimal(199), "electricity bill"),
                        new Transaction(users.get(3).getId(), LocalDateTime.of(2020, 12, 12, 12, 00, 00), "NL58INHO0123456702", "NL58INHO0123456701", new BigDecimal(500), "loan")
                );
        transactions.forEach(transactionRepository::save);
    }

}
