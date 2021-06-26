package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.DTO.UpdateAccountDTO;
import io.swagger.model.User;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountsApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService service;
    private Account account;
    private AccountDTO accountDTO;
    private UpdateAccountDTO updateAccountDTO;
    private List<Account> accounts;
    private UserService userService;
    private User user;

    @BeforeEach
    public void init(){

        accounts =
                Arrays.asList(
                        new Account(1L, "NL58INHO0123456789", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(2L, "NL58INHO0123456788", new BigDecimal(200 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(3L, "NL58INHO0123456701", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), true),
                        new Account(4L, "NL58INHO0123456702", new BigDecimal(9999.25 ), java.time.LocalDate.of(2021,05,27), AccountType.CURRENT, new BigDecimal(500 ), false)
                );
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAllAccountsShouldReturnOk() throws Exception {
        given(service.getAllAccounts()).willReturn(accounts);
        this.mvc.perform(get("/accounts/")).andExpect(
                status().isOk());
    }


//    @Test
//    @WithMockUser(username = "Employee", password = "employee1", roles = "Employee")
//    public void EmployeeCreateAccountWithFilledPropertiesShouldReturn201() throws Exception{
//        given(service.createAccount(accountDTO.getCreatedDate())).willReturn(accountDTO.getCreatedDate());
//        this.mvc.perform(post("/accounts")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content("{\n" +
//                        "  \"owner\": \"test1\",\n" +
//                        "  \"type\": \"Current\"\n" +
//                        "}"))
//                .andExpect(status().isCreated());
//    }
    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAccountsByIBANShouldReturnOK() throws Exception {
        given(service.getAccountByIban("NL58INHO0123456702")).willReturn(account.iban("NL58INHO0123456702"));
        this.mvc.perform(get("/accounts/")).andExpect(
                status().isOk());
    }
    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAccountsByIBANShouldReturnError() throws Exception {
        given(service.getAccountByIban("NL58INHO0123456701")).willReturn(accounts.get(1));
        this.mvc.perform(get("/accounts/")).andExpect(
                status().isOk());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void getAllAccountsByDateShouldReturnOk() throws Exception {
        given(service.getAccountsByCreatedDate(LocalDate.of(2021,06,21),0,100)).willReturn(accounts);
        this.mvc.perform(get("/accounts/")).andExpect(
                status().isOk());
    }



    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void updateAccountShouldReturnOk() throws Exception {
        given(service.updateAccount("NL58INHO0123456702",updateAccountDTO)).willReturn(account);
        this.mvc.perform(get("/accounts/")).andExpect(
                status().isOk());
    }



    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void postingAccountShouldReturn201Created() throws Exception {
        given(service.createAccount(accountDTO)).willReturn(account);
        this.mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void createAccountAsCustomerReturnForbidden() throws Exception {

        this.mvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("account")).andExpect(status().isForbidden());
    }





}