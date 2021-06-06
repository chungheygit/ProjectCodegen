package io.swagger.model;

import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
        private User user;

        @BeforeEach
        public void init(){
            user = new User();
        }

        @Test
        public void setIdShouldUpdateId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
        }

        @Test
        public void createUserShouldNotBeNull(){
            Assertions.assertNotNull(user);
        }

        @Test
        public void creatingAUserShouldReturnNumberOfStringsSix(){
        Assertions.assertEquals(6, user.getNumberOfStrings());
        }

        @Test
        public void setAbsoluteLimitShouldUpdateAbsoluteLimit() {
        user.setDayLimit(BigDecimal.valueOf(200));
        assertEquals(BigDecimal.valueOf(200), user.getDayLimit());
        }

        @Test
        public void setNameShouldUpdateName() {
        user.setFirstName("test");
        assertEquals("test", user.getFirstName());
        }













}
