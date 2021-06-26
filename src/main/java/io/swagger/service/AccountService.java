package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.DTO.AccountDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import javax.persistence.GeneratedValue;
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
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    String bank = "NL01INHO0000000001";


    UpdateAccountDTO updateAccountDTO;
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService() {

        updateAccountDTO = new UpdateAccountDTO();

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

//        validateAccountDTO(accountDTO); // validatie methode in controller

        account.setIban(generateIban()); // iban automatisch genereren en vullen
        account.setCreatedDate(java.time.LocalDate.now()); // Datum ter plek genereren

         return accountRepository.save(account);
    }
    // validate input create account
    public void validateAccountDTO(AccountDTO accountDTO) throws Exception {
        if (accountDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {// check of BALANCE niet negatief is
            log.error("Balance cannot be negative ! ");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Balance cannot be negative ! ");
        }
        if (accountDTO.getAbsoluteLimit().compareTo(BigDecimal.ZERO) < 0) {// check of ABSOLUTE-LIMIT niet negatief is
            log.error("Absolute limit cannot be negative ! ");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Absolute limit cannot be negative ! ");
        }
        // checkt of user id is in gebruik
        CheckUserId(accountDTO);

        if (accountDTO.getAccountType() == null)
        {
            log.error("Accounttype has to be 'current' or 'saving' ! ");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accounttype has to be 'current' or 'saving' ! ");
        }

    }
    public Account updateAccount(String iban, UpdateAccountDTO updateAccountDTO) throws Exception
    {
        Account accountToUpdate = getAccountByIban(iban);

        //validateUpdateAccountDTO(updateAccountDTO);// input validatie in controller


        // de parameters hieronder mogen worden geupdate, andere als Id en Iban blijven vast
        accountToUpdate.setAccountType(updateAccountDTO.getAccountType());
        accountToUpdate.setOpen(updateAccountDTO.getOpen());
        accountToUpdate.setAbsoluteLimit(updateAccountDTO.getAbsoluteLimit());
        accountToUpdate.setBalance(updateAccountDTO.getBalance());


        return  accountRepository.save(accountToUpdate);
    }
    // validate input update account
    public void validateUpdateAccountDTO(UpdateAccountDTO updateAccountDTO) throws Exception {

        //  check of BALANCE niet negatief is
        if (updateAccountDTO.getBalance().compareTo(BigDecimal.ZERO)  < 0)
        {
            log.error("Balance cannot be negative ! ");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Balance cannot be negative ! ");
        }
        if (updateAccountDTO.getAbsoluteLimit().compareTo(BigDecimal.ZERO) < 0) { //check of ABSOLUTE-LIMIT niet negatief is
            log.error("Absolute limit cannot be negative ! ");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Absolute limit cannot be negative ! ");
        }
        if (updateAccountDTO.getAccountType() == null)
        {
            log.error("Accounttype has to be 'current' or 'saving' ! ");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accounttype has to be 'current' or 'saving' ! ");
        }
    }
    // check if user exists
    public boolean userExist(Long userID) {
        return (userRepository.findById(userID)!= null);
    }
    public void CheckUserId(AccountDTO accountDTO) throws Exception {
        // check if user if exists
        Integer HaveToChooseId = Math.toIntExact(userRepository.findAll().size() + 1);
        Integer FilledID = Math.toIntExact(accountDTO.getUserId());
        if (userExist(accountDTO.getUserId()) == true && FilledID < HaveToChooseId)
        {
            log.error("User ID is already in use, please choose "+ HaveToChooseId );
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ID is already in use, please choose "+ HaveToChooseId);
        }
    }

        public Boolean EnumStartsWith(AccountDTO accountDTO)
       {
           String Type = accountDTO.getAccountType().toString();
           if (Type.startsWith("s"))
           {
               accountDTO.setAccountType(AccountType.SAVINGS);
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


//    public User checkIfUserIdIsUsed(User userId) throws Exception {
//        Integer LastUserId = userService.getAllUsers(100,0).size()-1;
//        if (userId.id() == LastUserId || userId.id() < LastUserId){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id already in use, choose id " + LastUserId);
//        }
//        return userId;
//    }

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
        if(!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)){
            throw new Exception("User not authorized");
        }
        else{
            return getAccountByIban(iban);
        }
    }
}

