package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsersApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void whenLoginUserShouldReturnJWTTokenAndOkStatus() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        String jwtToken = "Bearer 123456789";
        given(userService.login(loginDTO)).willReturn(jwtToken);
        mvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(jwtToken));
    }
}