package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.service.AccountService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    public List<Account> GetAllAccounts() {
        return accountService.GetAllAccounts();
    }
    public void deleteAccount(Account account){
       accountService.deleteAccount(account);
    }
    public void addAccount(Account account){
        accountService.addAccount(account);
    }
}
