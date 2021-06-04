package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.Transaction;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import org.springframework.stereotype.Service;
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

    public Transaction createTransaction(Transaction transaction) throws Exception{

        Account sender = accountService.getAccountByIban(transaction.getSender());
        Account receiver = accountService.getAccountByIban(transaction.getReceiver());

        if(sender.getAccountType() == AccountType.SAVINGS || receiver.getAccountType() == AccountType.SAVINGS)
        {
            if(!sender.getUserId().equals(receiver.getUserId())){
                throw new Exception("Forbidden to transfer from/to savings account from another user.");
            }
            else{
                if(sender.getAccountType() == AccountType.SAVINGS){
                    transaction.setDescription("WITHDRAWAL - " + transaction.getDescription());
                }
                else{
                    transaction.setDescription("DEPOSIT - " + transaction.getDescription());
                }
            }
        }
        //check limits!!!!!

        transaction.setTimestamp(LocalDateTime.now());
        //userPerforming!!!!!!!
        accountService.addToBalance(receiver, transaction.getAmount());
        accountService.subtractFromBalance(sender, transaction.getAmount());

        accountRepository.save(receiver);
        accountRepository.save(sender);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByIban(String iban, Integer offset, Integer limit, LocalDate startDate, LocalDate endDate){

        return transactionRepository.getTransactionsByIban(offset, limit, startDate, endDate,iban);
    }
//    public List<Transaction> getFilteredTransaction(Integer offset, Integer limit, LocalDate startDate, LocalDate endDate){
//        return transactionRepository.getFilteredTransactions(offset,limit,startDate,endDate);
//    }


}
