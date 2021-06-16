package io.swagger.repository;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("select a from Account a where a.iban = ?1")
    public Account getAccountByIban(String iban);

    @Query(value = "select * from Account  where created_date =?1  limit ?3 offset ?2 " , nativeQuery = true)
    public List<Account> getAccountByCreatedDate(LocalDate createdDate, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Account  WHERE user_Id =?1 " , nativeQuery = true)
    List<Account> getAccountByUserId(Long userId);
}

