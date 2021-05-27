package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    AccountRepository accountRepository;

    // List of all IBANs saved to just use any IBAN one time
    ArrayList<String> usedIBANs;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        usedIBANs = new ArrayList<>();
    }
    public List<Account> GetAllAccounts(){
        return accountRepository.findAll();
    }
    public Account findAccountByCreatedDate(LocalDate date){
        return accountRepository.findAccountByCreatedDate(date);
    }
    public void updateAccount(Account account){
        accountRepository.save(account);
    }
    public void addAccount(Account account){
         accountRepository.save(account);
    }
    public String generateIban(){
        Random random = new Random();
        String prefix1 = "NL";
        String prefix2 = "INHO0"; // contains one of the 10 numbers: 0
        String twoDigit;
        String tenDigit1, tenDigit2, tenDigit3;
        twoDigit = String.format("%02d", random.nextInt(100));
        tenDigit1 = String.format("%03d", random.nextInt(1000));
        tenDigit2 = String.format("%04d", random.nextInt(10000));
        tenDigit3 = String.format("%02d", random.nextInt(100));
        return prefix1 + twoDigit  + prefix2  + tenDigit1  + tenDigit2  + tenDigit3;
    }
   // public Boolean ibanExists(String IBAN){
     //   return accountRepository.findById(IBAN).isPresent() || usedIBANs.contains(IBAN);
   // }
 // public Account GetAccountByIban(String accountNumber)
  //{
   //   return accountRepository.findById(accountNumber).get();
  //}
}

