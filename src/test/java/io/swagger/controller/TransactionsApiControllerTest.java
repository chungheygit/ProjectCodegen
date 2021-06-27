package io.swagger.controller;

import io.swagger.model.*;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.threeten.bp.OffsetDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsApiControllerTest {

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
                        new User("N'Golo", "Kanté", LocalDate.of(2021,3,18), "emp","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), false)
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
        transaction = new Transaction(1L, LocalDateTime.of(2020, 12, 28, 12, 00, 00), "NL58INHO0999999999", "NL58INHO0123456788", new BigDecimal(100), "test");
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAllTransactionsShouldReturnOk() throws Exception {
        given(transactionService.getTransactionsByFilters(null, null, null, null, null)).willReturn(transactions);
        this.mvc.perform(get("/transactions/")).andExpect(
                status().isOk());
    }

    @Test
    @WithMockUser(username = "cus", password = "password", roles = "Customer")
    public void getAllTransactionsAsCustomerShouldReturnUnAutorized() throws Exception {
        given(transactionService.getTransactionsByFilters(null, null, null, null, null)).willReturn(transactions);
        this.mvc.perform(get("/transactions/")).andExpect(
                status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAllTransactionsByInvalidDateTimeShouldReturnBadRequest() throws Exception {
        given(transactionService.getTransactionsByFilters(null, null, null, "12/12/2222 12:00:00", null));
        this.mvc.perform(get("/transactions/")).andExpect(
                status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAllTransactionsByStartTimeGreaterThanEndTimeShouldReturnBadRequest() throws Exception {
        given(transactionService.getTransactionsByFilters(null, null, null, "12/12/2020 12:00:00", "12/12/2019 12:00:00"));
        this.mvc.perform(get("/transactions/")).andExpect(
                status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAllTransactionsByInvalidDateFormatTimeShouldReturnBadRequest() throws Exception {
        given(transactionService.getTransactionsByFilters(null, null, null, "2222", null));
        this.mvc.perform(get("/transactions/")).andExpect(
                status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getTransactionById1ShouldReturnOk() throws Exception{
        //given(transactionService.getTransactionById(transactions.get(1).getId())).willReturn(transactions.get(1));
        this.mvc.perform(get("/transactions/1")).andExpect(
                status().isOk());
    }

    @Test
    @WithMockUser(username = "cus", password = "password", roles = "Customer")
    public void getTransactionAsCustomerShouldReturnUnAutorized() throws Exception{
        this.mvc.perform(get("/transactions/1")).andExpect(
                status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getTransactionByNegativeIdShouldReturnBadRequest() throws Exception{
        this.mvc.perform(get("/transactions/-5")).andExpect(
                status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getTransactionByStringAsIdShouldReturnBadRequest() throws Exception{
        this.mvc.perform(get("/transactions/string")).andExpect(
                status().isBadRequest());
    }



}