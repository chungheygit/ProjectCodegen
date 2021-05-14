package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public List<Account> GetAllAccounts(){
        return accountRepository.findAll();
    }
    public void deleteAccount(Account account){
        accountRepository.delete(account);
    }
    public void addAccount(Account account){
         accountRepository.save(account);
    }

}

