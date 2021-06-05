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
    @Query(value = "SELECT * FROM Transaction WHERE sender = ?1 or receiver = ?1 AND timestamp >= ?4 and timestamp <= ?5 limit ?3 offset ?2", nativeQuery = true)
    public List<Transaction> getTransactionsByIban(String iban, Integer offset, Integer limit, LocalDate startDate, LocalDate endDate);
    //List<Transaction> getTransactionsByFilters(Integer offset, Integer limit, LocalDate startDateTime, LocalDate endDateTime);
}
