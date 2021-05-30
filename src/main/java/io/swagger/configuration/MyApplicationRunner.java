package io.swagger.configuration;

import io.swagger.model.User;
import io.swagger.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
//import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class MyApplicationRunner implements ApplicationRunner {


    private UserService userService;

    public MyApplicationRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Create one new user
        User user1 = new User(1L, "test", "test", LocalDate.of(2017, 1, 13), "lio","password", User.UserTypeEnum.EMPLOYEE, new BigDecimal("100.02"), new BigDecimal("200.02"), false);

        // Add the shops
        userService.createUser(user1);

    }
}
