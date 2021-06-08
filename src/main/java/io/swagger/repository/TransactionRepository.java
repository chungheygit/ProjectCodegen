package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM Transaction WHERE timestamp BETWEEN ?2 AND ?3 AND  Id IN ( SELECT Id FROM Transaction WHERE SENDER = ?1 OR RECEIVER = ?1) limit ?4 offset ?5", nativeQuery = true)
    List<Transaction> getTransactionsByFilters(String iban, LocalDate startDate, LocalDate endDate, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE timestamp BETWEEN ?1 AND ?2 limit ?3 offset ?4", nativeQuery = true)
    List<Transaction> getTransactionsByDay(LocalDate startDate, LocalDate endDate, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE SENDER = ?1 OR RECEIVER = ?1 limit ?2 offset ?3", nativeQuery = true)
    List<Transaction> getTransactionsByIban(String iban, Integer limit, Integer offset);

    @Query(value = "SELECT SUM(amount) FROM Transaction WHERE sender = ?1 AND timestamp LIKE ?2%", nativeQuery = true)
    public Double getSpentMoneyByDate(String iban, LocalDate date);
}
