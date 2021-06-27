package io.swagger.service;

import io.cucumber.java.bs.A;
import io.swagger.configuration.MyApplicationRunner;
import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.Transaction;
import io.swagger.model.DTO.UpdateAccountDTO;

import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.MyUserDetailsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

     private final static String BANK_IBAN = "NL01INHO0000000001";


    UpdateAccountDTO updateAccountDTO;
    AccountDTO accountDTO;
    Account account;
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService() {

        updateAccountDTO = new UpdateAccountDTO();
        accountDTO = new AccountDTO();
        account = new Account();

    }

    public List<Account> getAccountsByUserId (Long userId) {
        return accountRepository.getAccountByUserId(userId);
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public List<Account> getAccountsByCreatedDate(java.time.LocalDate date, Integer offset, Integer limit){
        if(date == null && offset == null && limit == null)
        {
            return getAllAccounts();
        }

        return (List<Account>) accountRepository.getAccountByCreatedDate(date, offset, limit);
    }

    public Account createAccount(AccountDTO accountDTO) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDTO, Account.class);

        account.setIban(generateIban()); // iban automatisch genereren en vullen
        account.setCreatedDate(java.time.LocalDate.now()); // Datum ter plek genereren

         return accountRepository.save(account);
    }

    public Account updateAccount(String iban, UpdateAccountDTO updateAccountDTO) throws Exception
    {
        Account accountToUpdate = getAccountByIban(iban);



        // de parameters hieronder mogen worden geupdate, andere als Id en Iban blijven vast
        accountToUpdate.setAccountType(updateAccountDTO.getAccountType());
        accountToUpdate.setOpen(updateAccountDTO.getOpen());
        accountToUpdate.setAbsoluteLimit(updateAccountDTO.getAbsoluteLimit());
        accountToUpdate.setBalance(updateAccountDTO.getBalance());


        return  accountRepository.save(accountToUpdate);
    }


    public void validateBalance(BigDecimal balance)
    {
        //  check of BALANCE niet negatief is
        if (balance.compareTo(BigDecimal.ZERO)  < 0)
        {
            log.error("Balance cannot be negative ! ");
            throw new IllegalArgumentException("Balance cannot be negative ! ");
        }
    }
    public void validateAbsoluteLimit(BigDecimal absoluteLimit)
    {
        if (absoluteLimit.compareTo(BigDecimal.ZERO) < 0) { //check of ABSOLUTE-LIMIT niet negatief is
            log.error("Absolute limit cannot be negative ! ");
            throw new IllegalArgumentException( "Absolute limit cannot be negative ! ");
        }
    }
    public void validateAccounttype(AccountType accountType)
    {
        if (accountType == null) // als er wat anders dan current or savings word ingevuld krijgt die een null terug
        {
            log.error("Accounttype has to be 'current' or 'savings' ! ");
            throw new IllegalArgumentException("Accounttype has to be 'current' or 'savings' ! ");
        }
    }
    public void ValidateUpdateBankAccount(String iban)
    {
        if (checkIfIbanEqualsBankIban(iban)== false)
        {
            log.error("bank account cannot be updated !");
            throw new IllegalArgumentException("bank account cannot be updated !");
        };
    }
    public void ValidateBooleanOpen(Boolean open)
    {
       if (checkIfOpenIsTrueOrFalse(open) == false )
       {
           log.error("Account has to be open or 'true' or 'false' !");
           throw new IllegalArgumentException("Account has to be open or 'true' or 'false' !");
       }
    }

    public boolean checkIfOpenIsTrueOrFalse(Boolean open)
    {
        if (open.equals(true) || open.equals(false) )
        {
            return true;

        }
        return false;
    }
    public boolean checkIfIbanEqualsBankIban(String iban)
    {
        if (iban.equals(BANK_IBAN) )
        {
            return false;

        }
        return true;
    }


    // check if user exists
    public boolean userExist(Long userID) {

        if (userRepository.findById(userID) == null)
        {
            return false;
        }
        return true;
    }


    // check if accounts exists
    public boolean accountExist(String iban) {
        return (accountRepository.getAccountByIban(iban) != null);
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
        String IBAN = prefix1 + twoDigit  + prefix2  + tenDigit1  + tenDigit2  + tenDigit3;
        while (accountExist(IBAN) == true)
        {
            generateIban();
        }
        return IBAN;
    }



    public static LocalDate parse(CharSequence text, DateTimeFormatter isoLocalDate) {
        return parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    protected void addToBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    protected void subtractFromBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
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
        if (!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)) {
            throw new Exception("User not authorized");
        } else {
            return getAccountByIban(iban);
        }
    }

    public boolean validIban(String iban) {
        Pattern pattern = Pattern.compile("NL\\d{2}INHO(\\d{10}|\\d{9})");
        Matcher matcher = pattern.matcher(iban);
        if(matcher.matches()){
            return true;
        }
        return false;
    }
    public boolean getAccountByIbanUserAuthorized(String iban) throws Exception {
        if(!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)){
            return false;
        }
        else{
            return true;
        }
    }
}

