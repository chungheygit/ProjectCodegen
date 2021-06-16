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
    @Autowired
    UpdateAccountDTO updateAccountDTO;


    String bank = "NL01INHO0000000001";



    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService() { }

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
    public Account updateAccount(String iban, UpdateAccountDTO updateAccountDTO) throws Exception
    {

        Account accountToUpdate = getAccountByIban(iban);
        // --------------CHECKEN--------------------
        // Rekening van de bank mag niet upgedated worden.
        if (accountToUpdate.getIban() == bank)
        {
            throw new Exception("Bank account cannot be updated");
        }

        //  1- check of BALANCE is niet negatief-- werkt
        if (updateAccountDTO.getBalance().compareTo(BigDecimal.ZERO)  < 0)
        {
            throw new Exception("Balance cannot be negative ! ");
        }
        //  2- check of ABSOLUTE-LIMIT niet negatief is
        if (updateAccountDTO.getAbsoluteLimit().compareTo(BigDecimal.ZERO)  < 0)
        {
            throw new Exception("Balance cannot be negative ! ");
        }

        // 4- Accounttype returnt een null waarde als niet de uiste enum wordt ingevoerd
        if (updateAccountDTO.getAccountType() == null)
        {
            // default zetten we de accountyype current
            updateAccountDTO.setAccountType(AccountType.CURRENT);
        }

        // ---------------VULLEN-----------------
        accountToUpdate.setAccountType(updateAccountDTO.getAccountType());
        accountToUpdate.setOpen(updateAccountDTO.getOpen());
        accountToUpdate.setAbsoluteLimit(updateAccountDTO.getAbsoluteLimit());
        accountToUpdate.setBalance(updateAccountDTO.getBalance());



        //-------------OPSLAAN EN RETURNEN--------
        return  accountRepository.save(accountToUpdate);

    }
    //    public void checkIfBalanceIsNegative()
//    {
//        if (updateAccountDTO.getBalance().compareTo(BigDecimal.ZERO)  < 0)
//        {
//            log.error("Balance cannot be negative ! ");
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance cannot be negative ! ");
//        }
//    }
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
    public Account createAccount(AccountDTO accountDTO) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDTO, Account.class);


        //  2- check of Absolut limit is niet negatief
        if (accountDTO.getAbsoluteLimit().compareTo(BigDecimal.ZERO)  < 0)
        {
            throw new Exception("Balance cannot be negative ! ");
        }
        //  2- check of Absolut limit is niet negatief
        if (accountDTO.getBalance().compareTo(BigDecimal.ZERO)  < 0)
        {
            throw new Exception("Balance cannot be negative ! ");
        }
        // 4- Accounttype returnt een null waarde als niet de juiste enum wordt ingevoerd
        if (accountDTO.getAccountType() == null)
        {
            // default zetten we de accountyype current
            accountDTO.setAccountType(AccountType.CURRENT);
        }

        // check if used id is used
//        if (userExist(accountDTO.getUserId()) == true)
//        {
//            throw new Exception("User ID is already used, please use other user ID ");
//        }
        // filling properties
        account.setIban(generateIban());
        account.setCreatedDate(java.time.LocalDate.now());




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
        String IBAN = prefix1 + twoDigit  + prefix2  + tenDigit1  + tenDigit2  + tenDigit3;
        while (accountExist(IBAN) == true)
        {
            generateIban();
        }
        return IBAN;
    }
    // check if accounts exists
    public boolean accountExist(String iban) {
        return (accountRepository.getAccountByIban(iban) != null);
    }

    // check if user exists
    public boolean userExist(Long userID) {
        return (userRepository.getOne(userID)!= null);
    }




//    public User checkIfUserIdIsUsed(User userId) throws Exception {
//        Integer LastUserId = userService.getAllUsers(100,0).size()-1;
//        if (userId.id() == LastUserId || userId.id() < LastUserId){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id already in use, choose id " + LastUserId);
//        }
//        return userId;
//    }
//    public void checkIfIbanIsFilledLikeIban(String Iban)
//    {
//        Account account = new Account();
//        String filledIban = account.getIban();
//        if (filledIban != account.getIban().)
//    }


    protected void addToBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    protected void subtractFromBalance(Account account, BigDecimal amount){
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }
}

