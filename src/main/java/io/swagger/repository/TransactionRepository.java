package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM Transaction limit ?1 offset ?2", nativeQuery = true)
    List<Transaction> getAllTransactionsByLimitAndOffset(Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE timestamp BETWEEN ?2 AND ?3 AND  Id IN ( SELECT Id FROM Transaction WHERE SENDER = ?1 OR RECEIVER = ?1) limit ?4 offset ?5", nativeQuery = true)
    List<Transaction> getAllTransactionsByFilters(String iban, Timestamp startDate, Timestamp endDate, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE timestamp >= ?1 limit ?2 offset ?3", nativeQuery = true)
    List<Transaction> getAllTransactionsByStartDate(Timestamp startDate, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE timestamp <= ?1 limit ?2 offset ?3", nativeQuery = true)
    List<Transaction> getAllTransactionsByEndDate(Timestamp endDate, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE timestamp >= ?1 AND timestamp <= ?2 limit ?3 offset ?4", nativeQuery = true)
    List<Transaction> getAllTransactionsByStartDateAndEndDate(Timestamp startDate, Timestamp endDate, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE SENDER = ?1 OR RECEIVER = ?1 limit ?2 offset ?3", nativeQuery = true)
    List<Transaction> getAllTransactionsByIban(String iban, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE SENDER = ?1 OR RECEIVER = ?1 And timestamp >= ?2 limit ?3 offset ?4", nativeQuery = true)
    List<Transaction> getAllTransactionsByIbanAndStartDate(String iban, Timestamp startDate, Integer limit, Integer offset);

    @Query(value = "SELECT * FROM Transaction WHERE SENDER = ?1 OR RECEIVER = ?1 And timestamp <= ?2 limit ?3 offset ?4", nativeQuery = true)
    List<Transaction> getAllTransactionsByIbanAndEndDate(String iban, Timestamp EndDate, Integer limit, Integer offset);

    @Query(value = "SELECT SUM(amount) FROM Transaction WHERE sender = ?1 AND timestamp LIKE ?2%", nativeQuery = true)
    public Double getSpentMoneyByDate(String iban, LocalDate date);
}
