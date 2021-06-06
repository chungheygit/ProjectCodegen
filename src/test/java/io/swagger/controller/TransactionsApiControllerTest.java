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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.threeten.bp.OffsetDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

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
                        new User("Bruno", "Fernandes", LocalDate.of(2021,1,25), "lio@test.com","password", UserType.ROLE_EMPLOYEE, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Frenkie", "De Jong", LocalDate.of(2021,4,20), "lio@test2.com","password", UserType.ROLE_CUSTOMER, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Kevin", "De Bruyne", LocalDate.of(2021,6,1), "cus","password", UserType.ROLE_CUSTOMER, new BigDecimal("1000.02"), new BigDecimal("250.02"), false),
                        new User("N'Golo", "Kanté", LocalDate.of(2021,3,18), "emp","password", UserType.ROLE_EMPLOYEE, new BigDecimal("1000.02"), new BigDecimal("250.02"), false)
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
    public void testMethode(){
    transactions.contains(new Transaction(users.get(3).getId(), LocalDateTime.of(2020, 12, 12, 12, 00, 00), "NL58INHO0123456702", "NL58INHO0123456701", new BigDecimal(500), "loan"));
    }

    @Test
    public void getAllTransactionsShouldReturnAJsonArray() throws Exception {
        given(transactionService.getAllTransactions()).willReturn(List.of(transaction));
        this.mvc.perform(
                get("/Transactions/getAllTransactionsUsingGET"))
                .andExpect(
                        status().isOk())
                .andExpect(jsonPath("$",hasSize(1))
                );
    }
    @Test
    public void callingTransaction1ShouldReturnOk() throws Exception {
        this.mvc.perform(get("/Transactions/getTransactionByIdUsingGET/transactions/1"))
                .andExpect(status().isOk());
    }

//    @Test
//    public void callingAllTransactionsShouldReturnOk() throws Exception {
//        given(transactionService.g("NL25INHO0123456789")).willReturn(transaction);
//
//        this.mvc.perform(get("/users/2/accounts/NL25INHO0123456789/transactions/")).andExpect(
//                status().isOk()).andExpect(jsonPath("$", hasSize(3)));
//    }

    @Test
    public void postingTransactionShouldReturn201Created() throws Exception {
        this.mvc.perform(
                MockMvcRequestBuilders
                        .post("/users/2/accounts/NL25INHO0625145897/transactions/")
                        .content("{\r\n  \"transactionDateTime\": \"2020-05-27T10:15:30+01:00\",\r\n  \"userPerforming\": {\r\n    \"id\": 2\r\n  },\r\n  \"amount\": 200.00,\r\n  \"accountTo\": {\r\n    \"iban\": \"NL25INHO0625145896\"\r\n  },\r\n  \"accountFrom\": {\r\n    \"iban\": \"NL25INHO0625145897\"\r\n  }\r\n}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("check if transaction can be a negative amount")
    public void testTransactionNegativeAmount() throws Exception {
        this.mvc.perform(post("/transactions")
                .content("{\n" +
                        "  \"AccountFrom\": \"NL58INHO0123456789\",\n" +
                        "  \"AccountTo\": \"NL58INHO0123456788\",\n" +
                        "  \"Amount\": -100.00\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

}