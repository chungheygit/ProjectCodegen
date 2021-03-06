package io.swagger.controller;


import io.swagger.api.UsersApi;
import io.swagger.api.UsersApiController;
import io.swagger.model.*;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.DTO.UpdateAccountDTO;

import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.authentication.BadCredentialsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsersApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    private List<User> users;
    private List<Account> accounts;
    private List<Transaction> transactions;

    private Transaction transaction;
    @BeforeEach
    public void init(){
        users =
                Arrays.asList(
                        new User("Bruno", "Fernandes", LocalDate.of(2021,1,25), "lio@test.com","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Frenkie", "De Jong", LocalDate.of(2021,4,20), "lio@test2.com","password", UserType.ROLE_Customer, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Kevin", "De Bruyne", LocalDate.of(2021,6,1), "cus","password", UserType.ROLE_Customer, new BigDecimal("1000.02"), new BigDecimal("250.02"), false),
                        new User("N'Golo", "Kant??", LocalDate.of(2021,3,18), "emp","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), false)
                );

        accounts =
                Arrays.asList(
                        new Account(users.get(0).getId(), "NL58INHO0123456789", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(users.get(1).getId(), "NL58INHO0123456788", new BigDecimal(200 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(users.get(2).getId(), "NL58INHO0123456701", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(users.get(3).getId(), "NL58INHO0123456702", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), false)
                );

        transactions =
                Arrays.asList(
                        new Transaction(users.get(0).getId(), LocalDateTime.now(), "NL58INHO0123456789", "NL58INHO0123456788", new BigDecimal(150), "water bill"),
                        new Transaction(users.get(1).getId(), LocalDateTime.now(), "NL58INHO0123456788", "NL58INHO0123456701", new BigDecimal(230), "taxes"),
                        new Transaction(users.get(2).getId(), LocalDateTime.of(2020, 12, 28, 12, 00, 00), "NL58INHO0123456701", "NL58INHO0123456702", new BigDecimal(199), "electricity bill"),
                        new Transaction(users.get(3).getId(), LocalDateTime.of(2020, 12, 12, 12, 00, 00), "NL58INHO0123456702", "NL58INHO0123456701", new BigDecimal(500), "loan")
                );
        transaction = new Transaction(1L, LocalDateTime.of(2020, 12, 28, 12, 00, 00), "NL58INHO0123456789", "NL58INHO0123456788", new BigDecimal(100), "test");
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithEmployeeRoleShouldReturnOk()  throws Exception{
        this.mvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": 100,\"email\": \"string@string.com\",\"firstName\": \"string\",\"lastName\": \"string\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "cus", password = "password", roles = "Customer")
    public void whenUpdateUserWithCustomerRoleShouldReturnUnAutorized()  throws Exception{
        this.mvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": 100,\"email\": \"string@string.com\",\"firstName\": \"string\",\"lastName\": \"string\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithInvalidDateOfBirthShouldReturnBadRequest()  throws Exception{
        this.mvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"2025-06-27\",\"dayLimit\": 100,\"email\": \"string@string.com\",\"firstName\": \"string\",\"lastName\": \"string\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithNegativeDayLimitShouldReturnBadRequest()  throws Exception{
        this.mvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": -100,\"email\": \"string@string.com\",\"firstName\": \"string\",\"lastName\": \"string\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithInvalidEmailShouldReturnBadRequest()  throws Exception{
        this.mvc.perform(put("/users/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": 100,\"email\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithNegativeTransactionLimitShouldReturnBadRequest()  throws Exception{
        this.mvc.perform(put("/users/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": 100,\"email\": \"string@string.com\",\"firstName\": \"string\",\"lastName\": \"string\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": -100,\"userType\": \"customer\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithInvalidPasswordShouldReturnBadRequest()  throws Exception{
        this.mvc.perform(put("/users/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": 100,\"email\": \"string@string.com\",\"firstName\": \"string\",\"lastName\": \"string\",\"open\": true,\"password\": \"string\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithInvalidFirstnameShouldReturnBadRequest()  throws Exception{
        this.mvc.perform(put("/users/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": 100,\"email\": \"string@string.com\",\"firstName\": \"1\",\"lastName\": \"string\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void whenUpdateUserWithInvalidLastnameShouldReturnBadRequest()  throws Exception{
        this.mvc.perform(put("/users/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"dateOfBirth\": \"1980-06-27\",\"dayLimit\": 100,\"email\": \"string@string.com\",\"firstName\": \"string\",\"lastName\": \"1\",\"open\": true,\"password\": \"String@123\",\"transactionLimit\": 100,\"userType\": \"customer\"}"))
                .andExpect(status().isBadRequest());
    }

}