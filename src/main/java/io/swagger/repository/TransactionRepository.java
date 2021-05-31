package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(value = "select * from Transaction where sender = ?1 or receiver = ?1 and timestamp >= ?4 and timestamp <= ?5 limit ?3 offset ?2", nativeQuery = true)
    public List<Transaction> getTransactionsByIban(String iban, Integer offset, Integer limit, LocalDate startDate, LocalDate endDate);
}
