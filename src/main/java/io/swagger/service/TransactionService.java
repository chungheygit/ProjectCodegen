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
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById (Integer transactionId) throws IllegalArgumentException{
        Optional<Transaction> optional = transactionRepository.findById(transactionId);
        return optional.orElseThrow(IllegalArgumentException::new);
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
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setUserPerforming(userPerforming.getId());

        //updating balance
        accountService.addToBalance(receiverAccount, transaction.getAmount());
        accountService.subtractFromBalance(senderAccount, transaction.getAmount());

        System.out.println("Sender: " + senderAccount.getBalance() + " receiver: " + receiverAccount.getBalance() );

        log.info("Transaction successfully created");
        return transactionRepository.save(transaction);
    }

    private TransactionType checkTransactionType(Account sender, Account receiver){
        if(sender.getAccountType() == AccountType.SAVINGS || receiver.getAccountType() == AccountType.SAVINGS)
        {
            if(!sender.getUserId().equals(receiver.getUserId())){
                log.error("User tried transfering to/from savings account from other user");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden to transfer from/to savings account from another user.");
            }
            else{
                if(sender.getAccountType() == AccountType.SAVINGS){
                    return TransactionType.WITHDRAWAL;
                }
                else{
                    return TransactionType.DEPOSIT;
                }
            }
        }
        else{
            return TransactionType.TRANSACTION;
        }
    }

    private void checkIfUserPerformingIsPermitted(User userPerforming, User senderUser){
        //check if user is employee
        if(!userService.IsLoggedInUserEmployee()){
            //check if user is sender
            if(!userPerforming.getId().equals(senderUser.getId())){
                log.error("User not employee, and not the owner of sending account");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not the sender");
            }
        }
    }

    private void checkIfAccountsAreTheSame(Account sender, Account receiver){
        if(sender == receiver){
            log.error("User tried sending money to same account");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't send money to same account");
        }
    }

    private void checkIfAccountsAreOpen(Account sender, Account receiver){
        if(!sender.isOpen() || !receiver.isOpen()){
            log.error("User tried sending money to a closed account");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't transfer to/from closed accounts");
        }
    }

    private void checkLimits(Transaction transaction, Account senderAccount, User senderUser){
        //transactionlimit
        if(transaction.getAmount().doubleValue() > senderUser.getTransactionLimit().doubleValue()){
            log.error("User exceed transaction limit");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount exceeded transaction limit");
        }

        //daylimit
        Double spentMoneyToday = transactionRepository.getSpentMoneyByDate(senderAccount.getIban(), LocalDate.now());
        if(spentMoneyToday==null){
            spentMoneyToday = 0.00;
        }
        if(spentMoneyToday > senderUser.getDayLimit().doubleValue()){
            log.error("User exceeded day limit");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount exceeded day limit");
        }

        //balance limit
        if(senderAccount.getBalance().doubleValue() - transaction.getAmount().doubleValue() < senderAccount.getAbsoluteLimit().doubleValue()){
            log.error("Account balance too low");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance too low");
        }
    }

    public List<Transaction> getTransactionsByFilters(String iban, Integer offset, Integer limit, String startDate, String endDate){

        //default values
        LocalDate parsedStartDate=LocalDate.of(1900,01,01);;
        LocalDate parsedEndDate=LocalDate.now();

        //if filters are not used. get
        if(iban!=null || offset!=null || limit!=null || startDate!=null || endDate!=null) {
            if (startDate != null || endDate != null) {
                if (startDate != null) {
                    parsedStartDate = LocalDate.parse(startDate);
                }
                if (endDate != null) {
                    parsedEndDate = LocalDate.parse(endDate);
                }
                if(iban==null) {
                    return transactionRepository.getTransactionsByDay(parsedStartDate,parsedEndDate,limit,offset);
                }
            } else if (iban != null) {
                //account ophalen
                Account account = accountRepository.getAccountByIban(iban);

                if (startDate == null) {
                    parsedStartDate = account.getCreatedDate();
                }
                return transactionRepository.getTransactionsByIban(iban, limit, offset);
            }

            return transactionRepository.getTransactionsByFilters(iban, parsedStartDate, parsedEndDate, limit, offset );
        }
        else{
            return getAllTransactions();
        }
        //return transactionRepository.getTransactionsByIban(iban, offset, limit, startDate, endDate);
    }


//    public List<Transaction> getFilteredTransaction(Integer offset, Integer limit, LocalDate startDate, LocalDate endDate){
//        return transactionRepository.getFilteredTransactions(offset,limit,startDate,endDate);
//    }


}
