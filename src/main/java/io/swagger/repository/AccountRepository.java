package io.swagger.repository;

import io.swagger.model.Account;
import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threeten.bp.LocalDate;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("SELECT a FROM Account a WHERE a.createdDate =: createdDate")
    Account findAccountByCreatedDate(@Param("createdDate") LocalDate createdDate);
}

