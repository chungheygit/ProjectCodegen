package io.swagger.repository;

import io.swagger.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByExistingEmailShouldNotReturnNull() throws Exception {
        assertTrue(userRepository.findUserByEmail("emp") != null);
    }

    @Test
    public void findUserByNonExistingEmailShouldReturnNull() throws Exception {
        assertNull(userRepository.findUserByEmail("nonexisting@gmai.com"));
    }

    @Test
    public void getUserByFilterslShouldNotReturnNull() throws Exception {
        assertTrue(userRepository.getUsersByFilters(1,1) != null);
    }

}
