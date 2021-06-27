package io.swagger.service;

import io.swagger.model.*;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.MyUserDetailsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService() {
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Integer transactionId) {
        return transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }

    public boolean IsUserPerformingIsPermitted(String senderIban) throws Exception {
        Account senderAccount = accountService.getAccountByIban(senderIban);
        User senderUser = userRepository.getOne(senderAccount.getUserId());
        User userPerforming = userService.findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());

        //check if user is employee
        if(!userService.IsLoggedInUserEmployee()){
            //check if user is sender
            if(!userPerforming.getId().equals(senderUser.getId())){
                return false;
            }
        }
        return true;
    }


    public void validateTransactionDTO(TransactionDTO transactionDTO){
        if(!accountService.validIban(transactionDTO.getSender())){
            log.error("Invalid iban entered");
            throw new IllegalArgumentException("Invalid iban entered");
        }
        if(!accountService.validIban(transactionDTO.getReceiver())){
            log.error("Invalid iban entered");
            throw new IllegalArgumentException("Invalid iban entered");
        }
        checkIfAmountIsLegit(transactionDTO.getAmount().doubleValue());
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception{

        ModelMapper modelMapper = new ModelMapper();
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);

        Account senderAccount = accountService.getAccountByIban(transaction.getSender());
        User senderUser = userRepository.getOne(senderAccount.getUserId());
        Account receiverAccount = accountService.getAccountByIban(transaction.getReceiver());
        User userPerforming = userService.findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());

        //checks
        checkIfAccountsAreTheSame(senderAccount, receiverAccount);
        checkIfAccountsAreOpen(senderAccount, receiverAccount);
        checkIfUserPerformingIsPermitted(userPerforming, senderUser);
        checkLimits(transaction, senderAccount, senderUser);

        //filling properties
        transaction.setTransactionType(checkTransactionType(senderAccount, receiverAccount));

        checkLimits(transaction, senderAccount, senderUser);

        //filling rest of properties
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setUserPerforming(userPerforming.getId());

        //updating balance
        accountService.addToBalance(receiverAccount, transaction.getAmount());
        accountService.subtractFromBalance(senderAccount, transaction.getAmount());

        System.out.println("Sender: " + senderAccount.getBalance() + " receiver: " + receiverAccount.getBalance());

        log.info("Transaction successfully created");
        return transactionRepository.save(transaction);
    }

    private TransactionType checkTransactionType(Account sender, Account receiver) {
        if (sender.getAccountType() == AccountType.SAVINGS || receiver.getAccountType() == AccountType.SAVINGS) {
            if (!sender.getUserId().equals(receiver.getUserId())) {
                log.error("User tried transfering to/from savings account from other user");
                throw new IllegalArgumentException("Forbidden to transfer from/to savings account from another user.");
            } else {
                if (sender.getAccountType() == AccountType.SAVINGS) {
                    return TransactionType.WITHDRAWAL;
                } else {
                    return TransactionType.DEPOSIT;
                }
            }
        } else {
            return TransactionType.TRANSACTION;
        }
    }

    private void checkIfUserPerformingIsPermitted(User userPerforming, User senderUser) {
        //check if user is employee
        if (!userService.IsLoggedInUserEmployee()) {
            //check if user is sender
            if (!userPerforming.getId().equals(senderUser.getId())) {
                log.error("User not employee, and not the owner of sending account");
                throw new IllegalArgumentException("You are not the sender");
            }
        }
    }

    private void checkIfAccountsAreTheSame(Account sender, Account receiver) {
        if (sender == receiver) {
            log.error("User tried sending money to same account");
            throw new IllegalArgumentException("Can't send money to same account");
        }
    }

    private void checkIfAccountsAreOpen(Account sender, Account receiver) {
        if (!sender.isOpen() || !receiver.isOpen()) {
            log.error("User tried sending money to a closed account");
            throw new IllegalArgumentException("Can't transfer to/from closed accounts");
        }
    }

    private void checkLimits(Transaction transaction, Account senderAccount, User senderUser){
        //dont check transactionlimit+daylimit on withdrawals and deposits
        if(transaction.getTransactionType()==TransactionType.TRANSACTION){
            //transactionlimit
            if(transaction.getAmount().doubleValue() > senderUser.getTransactionLimit().doubleValue()){
                log.error("User exceed transaction limit");
                throw new IllegalArgumentException("Amount exceeded transaction limit");
            }

            //daylimit
            Double spentMoneyToday = transactionRepository.getSpentTransactionMoneyByDate(senderAccount.getIban(), LocalDate.now());
            if(spentMoneyToday==null){
                spentMoneyToday = 0.00;
            }
            if(spentMoneyToday > senderUser.getDayLimit().doubleValue()){
                log.error("User exceeded day limit");
                throw new IllegalArgumentException("Amount exceeded day limit");
            }
        }

        //balance limit
        if (senderAccount.getBalance().doubleValue() - transaction.getAmount().doubleValue() < senderAccount.getAbsoluteLimit().doubleValue()) {
            log.error("Account balance too low");
            throw new IllegalArgumentException("Balance too low");
        }
    }

    public Boolean validateTransactionId(String transactionId) {
        //mag niet negatief en moet integer zijn

        try {
            Integer intValue = Integer.parseInt(transactionId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Boolean isTransactionFromLoggedInUser(Integer transactionid) {
        //getting the transaction
        Transaction transaction = getTransactionById(transactionid);

        //getting the loggedIn user
        User loggedInUser = userService.findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());

        //getting the accounts of loggedIn user
        List<Account> accountsOfLoggedInUser = accountService.getAccountsByUserId(loggedInUser.getId());

        // checks if sender is equal to loggedinuser

        for (Account a : accountsOfLoggedInUser) {

            for (int i = 0; i < accountsOfLoggedInUser.size(); i++) {

                //checks first if account is of loggedIn user
                if (a.getUserId().equals(loggedInUser.getId())) {
                    //checks if iban of account is equal to iban of receiver or sender
                    if (a.getIban().equals(transaction.getReceiver()) || a.getIban().equals(transaction.getSender())) {
                        return true;
                    }

                }
            }
        }
        return false;
    }
    private void checkIfTimestampIsValid(String timestamp) throws Exception {

        Timestamp parsedTimeStamp= convertToTimestamp(timestamp);

        //if Timestamp is greater than now
        if(parsedTimeStamp.compareTo(Timestamp.valueOf(LocalDateTime.now()))>0){
            log.error("DateTime can not be greater than now!");
            throw new IllegalArgumentException("DateTime can not be greater than now. Try Again!");
        }

    }
    private void checkIfStartDateIsGreaterThanEndDate(String startDateTime, String endDateTime) throws Exception {

        Timestamp parsedStartDateTime= convertToTimestamp(startDateTime);
        Timestamp parsedEndDateTime= convertToTimestamp(endDateTime);

        //if StartDateTime is greater than EndDateTime
        if(parsedStartDateTime.compareTo(parsedEndDateTime)>0){
            log.error("StartDateTime can not be greater than endDateTime!");
            throw new IllegalArgumentException("StartDateTime can not be greater than endDateTime. Try Again!");
        }
    }
    private void validateStartDateTimeAndEndDateTime(String startDateTime, String endDateTime) throws Exception {
        if (startDateTime != null && endDateTime != null) {
            checkIfStartDateIsGreaterThanEndDate(startDateTime, endDateTime);
        } else if (startDateTime != null) {
            checkIfTimestampIsValid(startDateTime);

        } else if (endDateTime != null) {
            checkIfTimestampIsValid(endDateTime);
        }
    }

    public List<Transaction> getTransactionsByFilters(String iban, Integer offset, Integer limit, String startDateTime, String endDateTime) throws Exception {

        //validate startDateTime en EndDateTime
        validateStartDateTimeAndEndDateTime(startDateTime,endDateTime);

        if (iban == null && offset == null && limit == null && startDateTime == null && endDateTime == null) {
            return getAllTransactions();
        } else if (iban != null && startDateTime == null && endDateTime == null) {
            return transactionRepository.getAllTransactionsByIban(iban, limit, offset);
        } else if (iban == null && startDateTime != null && endDateTime == null) {
            return transactionRepository.getAllTransactionsByStartDate(convertToTimestamp(startDateTime), limit, offset);
        } else if (iban == null && startDateTime == null && endDateTime != null) {
            return transactionRepository.getAllTransactionsByEndDate(convertToTimestamp(endDateTime), limit, offset);
        } else if (iban != null && startDateTime != null && endDateTime == null) {
            return transactionRepository.getAllTransactionsByIbanAndStartDate(iban,convertToTimestamp(startDateTime), limit, offset);
        } else if (iban != null && startDateTime == null && endDateTime != null) {
            return transactionRepository.getAllTransactionsByIbanAndEndDate(iban,convertToTimestamp(endDateTime), limit, offset);
        } else if (iban == null && startDateTime != null && endDateTime != null) {
            return transactionRepository.getAllTransactionsByStartDateAndEndDate(convertToTimestamp(startDateTime), convertToTimestamp(endDateTime), limit, offset);
        } else if (iban == null && startDateTime == null && endDateTime == null || limit != null || offset != null) {
            return transactionRepository.getAllTransactionsByLimitAndOffset(limit, offset);
        }
        return transactionRepository.getAllTransactionsByFilters(iban, convertToTimestamp(startDateTime), convertToTimestamp(endDateTime), limit, offset);
    }

    private void checkIfAmountIsLegit(double amount){
        if(amount < 0.01){
            log.error("User entered illegal amount value");
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public Timestamp convertToTimestamp(String date) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            return new Timestamp(dateFormat.parse(date).getTime());

        } catch (ParseException parseException) {
            throw new IllegalArgumentException ("Date has an invalid format. Message:" + parseException);
        }
    }
}

