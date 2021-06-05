package io.swagger.service;

import io.swagger.api.UsersApiController;
import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.model.UserType;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.MyUserDetailsService;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    MyUserDetailsService myUserDetailsService;

    AccountRepository accountRepository;

    UserRepository userRepository;
    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);


    // List of all IBANs saved to just use any IBAN one time
    ArrayList<String> usedIBANs;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
        else if (Iban != null && user.getUserType() == UserType.ROLE_CUSTOMER)
        {
            throw new Exception("No access for customers to update account details");
        }
        return  accountRepository.save(account);

    }

    public static LocalDate parse(CharSequence text, DateTimeFormatter isoLocalDate) {
        return parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }


    public Account getAccountByIban(String iban) throws Exception {
        Account account = accountRepository.getAccountByIban(iban);

        if(account==null){
            log.error("Attempting to get non-existing account");
            throw new Exception("Account does not exist");
        }
        return account;
    }

    //makes sure a customer can only retrieve his own accounts
    public Account getAccountByIbanWithSecurity(String iban) throws Exception {
        if(!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)){
            log.error("User tried getting account from other user");
            throw new Exception("This account does not belong to you");
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
        account.setBalance(amount.add(account.getBalance()));
        accountRepository.save(account);
    }

    protected void subtractFromBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }


}

