package io.swagger.service;

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

import java.util.List;
import java.util.Optional;

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

    public User findUserByEmail(String email)
    {
        return userRepository.findUserByEmail(email);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User createUser (User user){ return userRepository.save(user);}

    public User getUserById (long id){ return userRepository.findById(id).get(); }


    public User updateUser(User targetUser) { return userRepository.save(targetUser); }

    public String login(LoginDTO loginDTO){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            User user = userRepository.findUserByEmail(loginDTO.getEmail());

            return "Bearer " + jwtTokenProvider.createToken(user.getEmail(), Arrays.asList(user.getUserType()));
        }catch (AuthenticationException exception){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "email/password invalid");
        }

    }

    public Boolean IsLoggedInUserEmployee(){
        User currentUser = findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());
        if(currentUser.getUserType() == UserType.ROLE_EMPLOYEE){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean IsIbanFromLoggedInUser(String iban){
        boolean isIbanFromLoggedInUser;
        User currentUser = findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());
        try{
            if(accountService.getAccountByIban(iban).getUserId() == currentUser.getId()){
                return true;
            }
        }catch (Exception e){
            log.error("Exception: " + e);
        }
        return false;
    }
}
