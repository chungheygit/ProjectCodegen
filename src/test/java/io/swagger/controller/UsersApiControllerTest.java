package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.User;
import io.swagger.model.UserType;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsersApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    private List<User> users;
    private User user;
    private User userWithSameEmail;
    private User userEmailWithoutConstraints;

    @BeforeEach
    public void setup() {
        users =
                Arrays.asList(
                        new User("Bruno", "Fernandes", LocalDate.of(2021,1,25), "mark@inholland.nl","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Frenkie", "De Jong", LocalDate.of(2021,4,20), "lio@test2.com","password", UserType.ROLE_Customer, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Kevin", "De Bruyne", LocalDate.of(2021,6,1), "cus","password", UserType.ROLE_Customer, new BigDecimal("1000.02"), new BigDecimal("250.02"), false),
                        new User("N'Golo", "Kanté", LocalDate.of(2021,3,18), "emp","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), true),
                        new User("Bank", "account", LocalDate.of(1999,9,19), "bank@bankgroep6.com","groep6bank", UserType.ROLE_Employee, new BigDecimal("1000000.00"), new BigDecimal("100000.00"), true)
                );

        user = new User("N'Golo", "Kanté", LocalDate.of(2021,3,18), "Kees@hotmail.com","keesPassword", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), true);
        userWithSameEmail =  new User("N'Golo", "Kanté", LocalDate.of(2021,3,18), "emp","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), true);
        userEmailWithoutConstraints = new User("User", "user", LocalDate.of(2021,3,18), "withoutConstraints","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), true);
    }


    @Test
    @WithMockUser(username="emp",roles={"Employee"})
    public void callingAllUsersWithoutFiltersShouldReturnOk() throws Exception {
        given(userService.getAllUsers(0, 0)).willReturn(users);
        this.mvc.perform(get("/users/")).andExpect(
                status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username="emp",roles={"Employee"})
    public void getUser4ShouldReturnOk() throws Exception {
        given(userService.getUserById(4L)).willReturn(user);
        this.mvc.perform(get("/users/4/")).andExpect(
                status().isOk());
    }

    @Test
    public void getUser4WithoutMockUserShouldReturnForbidden() throws Exception {
        given(userService.getUserById(4L)).willReturn(user);
        this.mvc.perform(get("/users/4/")).andExpect(
                status().isForbidden());
    }

    @Test
    @WithMockUser(username="emp",roles={"Employee"})
    public void postingUserShouldReturn201Created() throws Exception {
        this.mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="emp",roles={"Employee"})
    public void postingUserWithSameEmailShouldReturn403Forbidden() throws Exception {
        this.mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithSameEmail)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="emp",roles={"Employee"})
    public void postingUserWithEmailWithoutConstraintsShouldReturn403Forbidden() throws Exception {
        this.mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEmailWithoutConstraints)))
                .andExpect(status().isForbidden());
    }


}