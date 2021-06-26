package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.User;
import io.swagger.model.UserType;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.security.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    // Get all users (With limit and offset)
    public List<User> getAllUsers(Integer limit, Integer offset) throws Exception {
        return userRepository.getUsersByFilters(limit, offset);
    }

    // Get a user by its ID
    public User getUserById(long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "No user found with userId " + id));
    }

    // Get all users by mail
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User createUser(User user) {
        //Encrypt password
        if (userRepository.findUserByEmail(user.getEmail()) == null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User targetUser) {
        return userRepository.save(targetUser);
    }

    public String login(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            User user = userRepository.findUserByEmail(loginDTO.getEmail());

            return "Bearer " + jwtTokenProvider.createToken(user.getEmail(), Arrays.asList(user.getUserType()));
        } catch (AuthenticationException exception) {
            // 403: Unauthorized client error status response code indicates that the request has not been applied because it lacks valid authentication credentials for the target resource
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email/password invalid");
        }

    }

    public Boolean IsLoggedInUserEmployee() {
        User currentUser = findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());
        if (currentUser.getUserType() == UserType.ROLE_Employee) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean IsIbanFromLoggedInUser(String iban) {
        User currentUser = findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());
        try {
            if (accountService.getAccountByIban(iban).getUserId() == currentUser.getId()) {
                return true;
            }
        } catch (Exception e) {
            log.error("Exception: " + e);
        }
        return false;
    }
}
