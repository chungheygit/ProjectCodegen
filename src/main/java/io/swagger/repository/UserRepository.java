package io.swagger.repository;

import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    @Query("SELECT u FROM User u WHERE u.email =:email")
    User findUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM User WHERE email =?1 limit ?3 offset ?2" , nativeQuery = true)
    List<User> getUsersByFilters(String email, Integer limit, Integer offset);


}
