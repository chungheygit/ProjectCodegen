package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;


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

    public Transaction createTransaction(Transaction transaction) throws Exception{

        Account sender = accountService.getAccountByIban(transaction.getSender());
        Account receiver = accountService.getAccountByIban(transaction.getReceiver());

        if(sender.getAccountType() == Account.AccountTypeEnum.SAVINGS || receiver.getAccountType() == Account.AccountTypeEnum.SAVINGS)
        {
            if(!sender.getUserId().equals(receiver.getUserId())){
                throw new Exception("Forbidden to transfer from/to savings account from another user.");
            }
            else{
                if(sender.getAccountType() == Account.AccountTypeEnum.SAVINGS){
                    transaction.setDescription("WITHDRAWAL - " + transaction.getDescription());
                }
                else{
                    transaction.setDescription("DEPOSIT - " + transaction.getDescription());
                }
            }
        }
        //check limits

        transaction.setTimestamp(OffsetDateTime.now());
        //userPerforming
        accountService.addToBalance(receiver, transaction.getAmount());
        accountService.subtractFromBalance(sender, transaction.getAmount());

        accountRepository.save(receiver);
        accountRepository.save(sender);
        return transactionRepository.save(transaction);
    }


}
