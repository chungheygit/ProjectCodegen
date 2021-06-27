package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.MyUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

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
            return GetAllAccounts();
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
        Account account = Optional.ofNullable(accountRepository.getAccountByIban(iban))
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        return account;
    }

    public boolean getAccountByIbanUserAuthorized(String iban) throws Exception {
        if(!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)){
            return false;
        }
        else{
            return true;
        }
    }

    public Account createAccount(AccountDTO accountDTO){

        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDTO, Account.class);


        // filling properties
        account.setIban(generateIban());
        account.setCreatedDate(java.time.LocalDate.now());
        account.setAbsoluteLimit(new BigDecimal(500));
        account.setAccountType(AccountType.CURRENT);
        account.setOpen(true);


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


    protected void addToBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    protected void subtractFromBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    public boolean validIban(String iban) {
        Pattern pattern = Pattern.compile("NL\\d{2}INHO(\\d{10}|\\d{9})");
        Matcher matcher = pattern.matcher(iban);
        if(matcher.matches()){
            return true;
        }
        return false;
    }
}

