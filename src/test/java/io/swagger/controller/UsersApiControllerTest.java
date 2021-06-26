package io.swagger.controller;

import io.swagger.api.UsersApi;
import io.swagger.api.UsersApiController;
import io.swagger.model.Account;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.DTO.UpdateAccountDTO;

import io.swagger.model.User;
import io.swagger.model.UserType;
import io.swagger.service.AccountService;
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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsersApiControllerTest {

    @Autowired
    private UsersApi api;
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    private User user;
    private User user1;

    @BeforeEach
    public void setUp() {
        //Spring boot login
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("emp","password"));
    }

    @Test
    //GetUserByID
    public void getUserByIdTest() throws Exception {
        Integer id = 4;
        ResponseEntity<User>responseEntity = api.getUserById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    //CreateUser
    @Test
    public void createAUserWithPasswordReturns201Created() throws Exception {
        User body = new User();
        body.setPassword("test");
        //body.setLastName("test");
        ResponseEntity<User> responseEntity = api.createUser(body);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }


    @Test
    //GetUserByExistingEmail
    public void createAUserWithExistingEmailReturnsException() throws Exception {
        User body = new User();
        body.setFirstName("hans");
        body.setEmail("cus");
        body.setId(100L);
        body.setPassword("password");
        body.setDayLimit( new BigDecimal(85000000.00 ));
        body.setDateOfBirth(LocalDate.of(2021,1,25));
        body.setOpen(false);
        body.setTransactionLimit( new BigDecimal(85000000.00 ));
        body.setUserType(UserType.ROLE_Employee);
        ResponseEntity<User> responseEntity = api.createUser(body);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    //GetAllUsers
    public void getUsersTest() throws Exception {
        Integer limit = 12;
        Integer offset = null;
        String email = "empp";
        ResponseEntity<List<User>> responseEntity = api.getAllUsers(offset, limit, email);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void updateAccountShouldReturnOk() throws Exception {
        given(userService.updateUser(user1)).willReturn(user);
        this.mvc.perform(get("/users/")).andExpect(
                status().isOk());
    }
}