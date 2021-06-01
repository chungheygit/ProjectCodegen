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
    @Query(value = "SELECT * FROM Transaction WHERE sender = ?5 or receiver = ?5 AND timestamp >= ?3 and timestamp <= ?4 limit ?2 offset ?1", nativeQuery = true)
    public List<Transaction> getTransactionsByIban(Integer offset, Integer limit, LocalDate startDate, LocalDate endDate,String iban);

    //List<Transaction> getTransactionsByFilters(Integer offset, Integer limit, LocalDate startDateTime, LocalDate endDateTime);
}
