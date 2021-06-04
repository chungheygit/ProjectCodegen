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

    @Query("select a from Account a where a.createdDate =:createdDate  ")
    public List<Account> getAccountByCreatedDate(LocalDate createdDate);
}

