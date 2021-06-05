package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    // List of all IBANs saved to just use any IBAN one time
    ArrayList<String> usedIBANs;

    public AccountService() {

        usedIBANs = new ArrayList<>();

    }
    public List<Account> GetAllAccounts(){
        return accountRepository.findAll();
    }
    public List<Account> getAccountByCreatedDate(java.time.LocalDate date, Integer offset, Integer limit){
        if(date == null && offset == null && limit == null)
        {
            date = java.time.LocalDate.now();
            offset= 0;
            limit = 10;
        }

        return (List<Account>) accountRepository.getAccountByCreatedDate(date, offset, limit);
    }
    public Account updateAccount(Account account) throws Exception
    {

        String Iban = account.getIban();
        if (Iban == null)
        {
            throw new Exception("Account does not exist");
        }

        return  accountRepository.save(account);

    }
    public static LocalDate parse(CharSequence text, DateTimeFormatter isoLocalDate) {
        return parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public Account getAccountByIban(String iban) throws Exception {
        Account account = accountRepository.getAccountByIban(iban);

        if(account==null){
            throw new Exception("Account does not exist");
        }
        return account;
    }

    //makes sure a customer can only retrieve his own accounts
    public Account getAccountByIbanWithSecurity(String iban) throws Exception {
        if(!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)){
            throw new Exception("User not authorized");
        }
        else{
            return getAccountByIban(iban);
        }
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
        BigDecimal newBalance = amount.add(account.getBalance());
        account.setBalance(newBalance);
        accountRepository.save(account);

    }

    protected void subtractFromBalance(Account account, BigDecimal amount){
        BigDecimal newBalance = amount.subtract(account.getBalance());
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}

