package io.swagger.repository;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threeten.bp.LocalDate;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("select a from Account a where a.iban = ?1")
    public Account getAccountByIban(String iban);

    @Query("select a from Account a where a.createdDate =:createdDate  ")
    public Account getAccountByCreatedDate(LocalDate createdDate);

}

