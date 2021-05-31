package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    AccountRepository accountRepository;

    UserRepository userRepository;
    UserService userService;

    // List of all IBANs saved to just use any IBAN one time
    ArrayList<String> usedIBANs;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        usedIBANs = new ArrayList<>();

    }
    public List<Account> GetAllAccounts(){
        return accountRepository.findAll();
    }
    public Account getAccountByCreatedDate(LocalDate date){
        if(date == null)
        {
            date = LocalDate.now();
        }

        return accountRepository.getAccountByCreatedDate(date);
    }
    public Account updateAccount(Account account) throws Exception
    {
        User user = userService.getUserById(account.getUserId());

        String Iban = account.getIban();
        if (Iban == null)
        {
            throw new Exception("Account does not exist");
        }
        else if (Iban != null && user.getUserType() == User.UserTypeEnum.CUSTOMER)
        {
            throw new Exception("No access for customers to update account details");
        }
        return  accountRepository.save(account);

    }

    public Account getAccountByIban(String iban) throws Exception {
        if(getAccountByIban(iban)==null){
            throw new Exception("Account does not exist");
        }
        return accountRepository.getAccountByIban(iban);
    }

    public Account createAccount(Account account){
         return accountRepository.save(account);
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

    protected void addToBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    protected void subtractFromBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }
}

