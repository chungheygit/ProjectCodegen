package io.swagger.service;

import io.swagger.model.Transaction;
import io.swagger.repository.TransactionRepository;
import org.springframework.stereotype.Service;


@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }
}
